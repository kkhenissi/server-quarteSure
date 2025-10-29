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
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiCustomServiceImpl implements AiCustomService {

    private final OpenAiService openAiService;

    public AiCustomServiceImpl(@Value("${OPENAI_API_KEY}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
    }
    @Override
    public String ask(String prompt) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-4o") // ou "gpt-4o"
                .messages(List.of(
                        new ChatMessage("system", "Tu es un assistant expert en analyse de performances de chevaux de course."),
                        new ChatMessage("user", prompt)
                ))
                .maxTokens(2000)
                .temperature(0.2)
                .build();

        ChatCompletionResult result = openAiService.createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent().toString();
    }

    @Override
    public RegressionResult calculateRegression(List<CourseData> courses) {
        if (courses == null || courses.size() < 2) {
            return new RegressionResult(0, 0, 0, 0);
        }

        // --- Construire les matrices X (variables explicatives) et y (résultat = positionArrivee)
        int n = courses.size();
        double[][] X = new double[n][4]; // speedChv, poids, corde,currentDistance, currentEtatTerrain
        double[] y = new double[n];      // positionArrivee

        for (int i = 0; i < n; i++) {
            CourseData c = courses.get(i);
            X[i][0] = c.getSpeedChv();
            X[i][1] = c.getHandicapPoids();
            X[i][2] = c.getPlaceCorde();
            X[i][3] = c.getCurrentDistance();
          //  X[i][4] = c.getCurrentEtatTerrain();
            y[i] = c.getPositionArrivee();
        }

//        // --- Régression linéaire multiple
//        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
//        regression.newSampleData(y, X);
//
//        double[] coefficients = regression.estimateRegressionParameters();
//        double r2 = regression.calculateRSquared();
//
//        // coefficients[0] = intercept (b)
//        // coefficients[1] = alpha (speedChv)
//        // coefficients[2] = beta (poids)
//        // coefficients[3] = gamma (corde)
//
//        return new RegressionResult(
//                coefficients[1],
//                coefficients[2],
//                coefficients[3],
//                r2
//        );

        MillerUpdatingRegression regression = new MillerUpdatingRegression(4, true);
        for (int i = 0; i < n; i++) {
            regression.addObservation(X[i], y[i]);
        }

        RegressionResults results = regression.regress();
        double[] coefficients = results.getParameterEstimates();
        double r2 = results.getRSquared();
                return new RegressionResult(
                coefficients[1],
                coefficients[2],
                coefficients[3],
                r2
        );
    }

    @Override
    public List<CourseData> predictAndSort(List<CourseData> courses) {
        RegressionResult result = calculateRegression(courses);

        for (CourseData c : courses) {
            double predictedSpeed =
                    result.getAlpha() * c.getSpeedChv() +
                            result.getBeta() * c.getHandicapPoids() +
                            result.getGamma() * c.getPlaceCorde();
            c.setPredictedSpeed(predictedSpeed);
        }

        // Tri décroissant sur vitesse prédite
        return courses.stream()
                .sorted(Comparator.comparingDouble(CourseData::getPredictedSpeed))
                .collect(Collectors.toList());
    }
}
