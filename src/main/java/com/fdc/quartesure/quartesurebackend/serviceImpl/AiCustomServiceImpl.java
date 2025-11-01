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
        if (historique == null || historique.size() < 2)
            return new RegressionResult(0, 0, 0, 0,0);

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();

        double[] y = historique.stream()
                .mapToDouble(CourseData::getPositionArrivee)
                .toArray();

        double[][] x = historique.stream()
                .map(c -> new double[]{c.getSpeedChv(), c.getHandicapPoids(), c.getCorde(), c.getPerfTendance()})
                .toArray(double[][]::new);

        regression.newSampleData(y, x);

        double[] coefficients;
        try {
            coefficients = regression.estimateRegressionParameters();
        } catch (Exception e) {
            return new RegressionResult(0, 0, 0, 0,0);
        }

        double alpha = coefficients[1];
        double beta = coefficients[2];
        double gamma = coefficients[3];
        double lambda = coefficients[4];
        double r2 = regression.calculateRSquared();

        return new RegressionResult(alpha, beta, gamma,lambda, r2);
    }

    @Override
    public Map<String, Object> predictAndSort(List<CourseData> courses) {
        RegressionResult regression = calculateRegression(courses);

        // Calcul de la vitesse prédite
        for (CourseData c : courses) {
            double predictedSpeed = regression.getAlpha() * c.getSpeedChv()
                    + regression.getBeta() * c.getHandicapPoids()
                    + regression.getGamma() * c.getPlaceCorde()
                    + regression.getLambda() * c.getPerfTendance();
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
                .sorted(Comparator.comparingDouble(CourseData::getPredictedSpeed))
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

        return response;
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

