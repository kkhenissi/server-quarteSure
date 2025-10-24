package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdc.quartesure.quartesurebackend.rest.AiRest;
import com.fdc.quartesure.quartesurebackend.service.AiCustomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AiRestImpl implements AiRest {

    private final AiCustomService aiService;
    private final ObjectMapper mapper = new ObjectMapper();

    public AiRestImpl(AiCustomService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ResponseEntity<Object> ask(Map<String, Object> body) {
        String prompt = (String) body.get("prompt");
        String rawResponse = aiService.ask(prompt);
        String cleanedResponse = rawResponse;

        try {
            // 🔹 Étape 1 : si la réponse est un JSON avec un champ "response"
            if (rawResponse.trim().startsWith("{") && rawResponse.contains("\"response\"")) {
                JsonNode root = mapper.readTree(rawResponse);
                if (root.has("response")) {
                    cleanedResponse = root.get("response").asText();
                }
            }

            // 🔹 Étape 2 : suppression des balises Markdown ```json ... ```
            cleanedResponse = cleanedResponse
                    .replaceAll("(?s).*```json", "")
                    .replaceAll("```.*", "")
                    .trim();

            // 🔹 Étape 3 : parse du vrai JSON propre
            Object json = mapper.readValue(cleanedResponse, Object.class);
            return ResponseEntity.ok(json);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid JSON from AI",
                    "rawResponse", rawResponse
            ));
        }
    }
}
