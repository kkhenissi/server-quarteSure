package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdc.quartesure.quartesurebackend.rest.AiRest;
import com.fdc.quartesure.quartesurebackend.service.AiCustomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AiRestImpl implements AiRest {

    private final AiCustomService aiService;

    public AiRestImpl(AiCustomService aiService) {
        this.aiService = aiService;
    }

    @Override
    public ResponseEntity<Map<String, Object>> ask(@RequestBody Map<String, Object> body) {
        try {
            String prompt = (String) body.get("prompt");
            String rawResponse = aiService.ask(prompt);

            // ✅ Si ton IA renvoie déjà un JSON, tu peux essayer de le parser
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonResponse;
            try {
                jsonResponse = mapper.readValue(rawResponse, Map.class);
            } catch (Exception e) {
                // Si ce n’est pas du JSON, on le met sous forme texte
                jsonResponse = Map.of("response", rawResponse);
            }

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("error", e.getMessage()));
        }
    }

//    @Override
//    public String ask(String prompt) {
//        return aiService.ask(prompt);
//    }
}
