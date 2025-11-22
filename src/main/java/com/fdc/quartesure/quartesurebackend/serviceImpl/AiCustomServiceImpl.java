package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.service.AiCustomService;
import com.fdc.quartesure.quartesurebackend.wrapper.CourseData;
import com.fdc.quartesure.quartesurebackend.wrapper.RegressionResult;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.stat.regression.MillerUpdatingRegression;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.stat.regression.RegressionResults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiCustomServiceImpl implements AiCustomService {

    @Override
    public RegressionResult calculateRegression(List<CourseData> historique) {
        if (historique == null || historique.isEmpty()) {
            return new RegressionResult(0, 0, 0,0);
        }

        try {
            int n = historique.size();

            // === Variables ===
            double[] y = new double[n]; // variable dépendante : positionArrivee
            double[][] X = new double[n][3]; // speed, poids, handicap, distance (par ex.)

            for (int i = 0; i < n; i++) {
                CourseData c = historique.get(i);
                y[i] = c.getPositionArrivee();
                X[i][0] = c.getSpeedChv();
                X[i][1] = c.getHandicapPoids();
                X[i][2] = c.getPerfTendance();
//                X[i][3] = c.getDistance();
//                X[i][4] = c.getPerfTendance();
            }

            // === Calcul des variances ===
            List<Integer> validCols = new ArrayList<>();
            for (int j = 0; j < X[0].length; j++) {
                double mean = 0, variance = 0;
                for (int i = 0; i < n; i++) mean += X[i][j];
                mean /= n;
                for (int i = 0; i < n; i++) variance += Math.pow(X[i][j] - mean, 2);

                if (variance > 1e-9) validCols.add(j); // on garde seulement les colonnes utiles
            }

            if (validCols.isEmpty()) {
                System.err.println("⚠️ Aucune variable utile (variance nulle).");
                return new RegressionResult(0, 0, 0,0);
            }

            // === Réduction du X ===
            double[][] Xfiltered = new double[n][validCols.size()];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < validCols.size(); j++) {
                    Xfiltered[i][j] = X[i][validCols.get(j)];
                }
            }

            // === Régression ridge (régularisée) ===
            RealMatrix Xmat = new Array2DRowRealMatrix(Xfiltered);
            RealVector Yvec = new ArrayRealVector(y);

            RealMatrix Xt = Xmat.transpose();
            RealMatrix XtX = Xt.multiply(Xmat);

            // lambda : régularisation
            double lambda = 1e-6;
            int p = XtX.getColumnDimension();
            RealMatrix XtXplusLambda = XtX.add(MatrixUtils.createRealIdentityMatrix(p).scalarMultiply(lambda));

            DecompositionSolver solver = new LUDecomposition(XtXplusLambda).getSolver();
            RealVector beta = solver.solve(Xt.operate(Yvec));

            double[] coeffs = beta.toArray();

            // === Calcul de R² ===
            RealVector Ypred = Xmat.operate(beta);
            double ssTot = 0.0, ssRes = 0.0;
            double meanY = Arrays.stream(y).average().orElse(0);
            for (int i = 0; i < n; i++) {
                ssTot += Math.pow(y[i] - meanY, 2);
                ssRes += Math.pow(y[i] - Ypred.getEntry(i), 2);
            }
            double r2 = 1 - ssRes / ssTot;

            // === Mapping des coefficients ===
            double alpha = coeffs.length > 0 ? coeffs[0] : 0;
            double betaCoeff = coeffs.length > 1 ? coeffs[1] : 0;
            double gamma = coeffs.length > 2 ? coeffs[2] : 0;
//            double lambdaCoeff = coeffs.length > 2 ? coeffs[3] : 0;

            return new RegressionResult(alpha, betaCoeff,gamma, r2);
          ///  , gamma,lambdaCoeff

        } catch (Exception e) {
            e.printStackTrace();
            return new RegressionResult(0, 0,0,0);
        }
    }

    @Override
    public RegressionResult trainBatch(List<List<CourseData>> dataset) {
//
//        double totalAlpha = 0;
//        double totalBeta = 0;
////        double totalGamma = 0;
////        double totalLambda = 0;
//        double totalR2 = 0;
//        int count = 0;
//
//        for (List<CourseData> course : dataset) {
//            if (course == null || course.isEmpty()) continue;
//            RegressionResult result = calculateRegression(course);
//            log.info("What is in result inside trainBatch {}", result);
//            totalAlpha += result.getAlpha();
//            totalBeta += result.getBeta();
////            totalGamma += result.getGamma();
////            totalLambda += result.getLambda();
//            totalR2 += result.getR2();
//            count++;
//        }
//
//        if (count == 0) return new RegressionResult(0, 0,0);
//    ////  , 0, 0,0);
//
//        return new RegressionResult(
//                totalAlpha / count,
//                totalBeta / count,
//
//                totalR2 / count
//                );
        try {
            // === Étape 1 : Fusionner toutes les courses en un seul bloc ===
            List<CourseData> allData = dataset.stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            log.info("What is in allData {}", allData);

            int n = allData.size();
            if (n < 3) {
                System.err.println("⚠️ Pas assez de données pour entraîner le modèle (" + n + ")");
                return new RegressionResult(0, 0, 0,0);
            }

            // === Étape 2 : Créer X (features) et y (cible) ===
            double[] y = new double[n]; // position d'arrivée
            double[][] X = new double[n][4]; // variables explicatives : speed, poids, tendance ...

            for (int i = 0; i < n; i++) {
                CourseData c = allData.get(i);
                y[i] = c.getPositionArrivee();
                X[i][0] = c.getSpeedChv();
                X[i][1] = c.getHandicapPoids();
                X[i][2] = c.getPerfTendance();
                X[i][3] = c.getScorePerformanceJockey();
            }

            // === Étape 3 : Supprimer les colonnes à variance nulle ===
            List<Integer> validCols = new ArrayList<>();
            for (int j = 0; j < X[0].length; j++) {
                double mean = 0, variance = 0;
                for (int i = 0; i < n; i++) mean += X[i][j];
                mean /= n;
                for (int i = 0; i < n; i++) variance += Math.pow(X[i][j] - mean, 2);
                if (variance > 1e-9) validCols.add(j);
            }

            if (validCols.isEmpty()) {
                System.err.println("⚠️ Aucune variable utile (variance nulle sur toutes les colonnes).");
                return new RegressionResult(0, 0, 0,0);
            }

            log.info("What is in allData {} ",allData);

            // === Étape 4 : Construire la matrice filtrée ===
            double[][] Xfiltered = new double[n][validCols.size()];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < validCols.size(); j++) {
                    Xfiltered[i][j] = X[i][validCols.get(j)];
                }
            }

            // === Étape 5 : Calcul de la régression Ridge (régularisée) ===
            RealMatrix Xmat = new Array2DRowRealMatrix(Xfiltered);
            RealVector Yvec = new ArrayRealVector(y);

            RealMatrix Xt = Xmat.transpose();
            RealMatrix XtX = Xt.multiply(Xmat);

            double lambda = 1e-6; // régularisation pour éviter la singularité
            int p = XtX.getColumnDimension();
            RealMatrix XtXplusLambda = XtX.add(MatrixUtils.createRealIdentityMatrix(p).scalarMultiply(lambda));

            DecompositionSolver solver = new LUDecomposition(XtXplusLambda).getSolver();
            RealVector beta = solver.solve(Xt.operate(Yvec));

            double[] coeffs = beta.toArray();

            // === Étape 6 : Calcul du coefficient de détermination R² ===
            RealVector Ypred = Xmat.operate(beta);
            double ssTot = 0.0, ssRes = 0.0;
            double meanY = Arrays.stream(y).average().orElse(0);
            for (int i = 0; i < n; i++) {
                ssTot += Math.pow(y[i] - meanY, 2);
                ssRes += Math.pow(y[i] - Ypred.getEntry(i), 2);
            }
            double r2 = 1 - ssRes / ssTot;

            // === Étape 7 : Extraire les coefficients ===
            double alpha = coeffs.length > 0 ? coeffs[0] : 0;
            double betaCoeff = coeffs.length > 1 ? coeffs[1] : 0;
            double gamma = coeffs.length > 2 ? coeffs[2] : 0;

            System.out.println("✅ Régression globale calculée avec succès :");
            System.out.println("   α=" + alpha + ", β=" + betaCoeff + ", γ=" + gamma + ", R²=" + r2);

            return new RegressionResult(alpha, betaCoeff,gamma, r2);

        } catch (Exception e) {
            e.printStackTrace();
            return new RegressionResult(0, 0, 0,0);
        }
    }

//    totalGamma / count,
//    totalLambda / count,

//    public List<CourseData> predictAndSort(List<CourseData> courses) {
//        RegressionResult result = calculateRegression(courses);
//
//        for (CourseData c : courses) {
//            double predicted = result.getAlpha() * c.getSpeedChv()
//                    + result.getBeta() * c.getPoids()
//                    + result.getGamma() * c.getCorde();
//            c.setPredictedSpeed(predicted);
//        }
//
//        // ✅ Tri décroissant (meilleurs en haut)
//        return courses.stream()
//                .sorted(Comparator.comparingDouble(CourseData::getPredictedSpeed).reversed())
//                .collect(Collectors.toList());
//    }
    @Override
    public List<CourseData> predictAndSort(List<CourseData> courses) {
        RegressionResult regression = calculateRegression(courses);

        // Calcul de la vitesse prédite
        for (CourseData c : courses) {
            double predictedSpeed = regression.getAlpha() * c.getSpeedChv()
                    + regression.getBeta() * c.getPerfTendance()
                    + regression.getGamma() * c.getHandicapPoids();
//                    + regression.getGamma() * c.getPlaceCorde()
//                    + regression.getLambda() * c.getPerfTendance();
            c.setPredictedSpeed(predictedSpeed);
//            log.info("valeur de regression.getAlpha() ==> {}",regression.getAlpha());
//            log.info("valeur de c.getSpeedChv() ==> {}",c.getSpeedChv());
//
//            log.info("*******************************************************************");
//            log.info("valeur de regression.getBeta() ==> {}",regression.getBeta());
//            log.info("valeur de c.getHandicapPoids() ==> {}",c.getHandicapPoids());
//
//            log.info("*******************************************************************");
//            log.info("valeur de regression.getGamma() ==> {}", regression.getGamma());
//            log.info("valeur de c.getCorde() ==> {}",c.getPlaceCorde());
//
            log.info("*******************************************************************");
//            log.info("valeur de regression.getLambda() ==> {}",regression.getLambda());
//            log.info("valeur de c.getPerfTendance() ==> {}",c.getPerfTendance());

            log.info("valeur de predictedSpeed ==> {}",predictedSpeed);


        }

        // Tri décroissant sur vitesse prédite (le plus rapide en premier)
        List<CourseData> sorted = courses.stream()
                .sorted(Comparator.comparingDouble(CourseData::getPredictedSpeed).reversed())
                .collect(Collectors.toList());

        // Attribution des rangs prévus
        for (int i = 0; i < sorted.size(); i++) {
            sorted.get(i).setRankPredicted(i + 1);
        }

        // Calcul du Spearman
        double spearman = computeSpearmanCorrelation(sorted);

        // Construction de la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("coefficients", regression);
        response.put("spearman", spearman);
        response.put("classement", sorted);


        return (List<CourseData>) response;
    }

    /**
     * Corrélation de Spearman entre classement prédit et arrivée réelle.
     */
    private double computeSpearmanCorrelation(List<CourseData> courses) {
        int n = courses.size();
        if (n < 2) return 0.0;

        // Rangs réels (1 = meilleur)
        Map<Integer, Integer> rankReal = new HashMap<>();
        List<CourseData> sortedByArrival = courses.stream()
                .sorted(Comparator.comparingInt(CourseData::getPositionArrivee))
                .toList();
        for (int i = 0; i < sortedByArrival.size(); i++) {
            rankReal.put(sortedByArrival.get(i).getNumPmu(), i + 1);
        }

        // Rangs prédits (1 = meilleur)
        List<CourseData> sortedByPred = courses.stream()
                .sorted(Comparator.comparingInt(CourseData::getRankPredicted))
                .toList();

        double sumDiff2 = 0.0;
        for (CourseData c : sortedByPred) {
            int rankPred = c.getRankPredicted();
            int rankTrue = rankReal.get(c.getNumPmu());
            double diff = rankPred - rankTrue;
            sumDiff2 += diff * diff;
        }

        double spearman = 1 - ((6.0 * sumDiff2) / (n * (n * n - 1)));
        return Math.max(-1, Math.min(1, spearman)); // sécurité numérique
    }
}

