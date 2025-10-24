package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.service.AiCustomService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class AiCustomServiceImpl implements AiCustomService {

    private final OpenAiService openAiService;

    public AiCustomServiceImpl(@Value("${openai.api.key}") String apiKey) {
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
}
