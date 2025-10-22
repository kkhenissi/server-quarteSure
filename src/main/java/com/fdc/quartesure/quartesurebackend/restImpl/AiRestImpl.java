package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.rest.AiRest;
import com.fdc.quartesure.quartesurebackend.service.AiCustomService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AiRestImpl implements AiRest {

    private final AiCustomService aiService;

    public AiRestImpl(AiCustomService aiService) {
        this.aiService = aiService;
    }

    @Override
    public String ask(Map<String, Object> body) {
        String prompt = (String) body.get("prompt");
    //    Object data = body.get("data");

        // on transforme ton objet JSON en texte pour l'envoyer à OpenAI
      //  String fullPrompt = prompt + "\nVoici les données : " + data.toString();

        return aiService.ask(prompt);
    }
//    @Override
//    public String ask(String prompt) {
//        return aiService.ask(prompt);
//    }
}
