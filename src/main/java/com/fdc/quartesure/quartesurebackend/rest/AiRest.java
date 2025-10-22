package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin
public interface AiRest {
//        @GetMapping(value = "/ask", produces = MediaType.TEXT_PLAIN_VALUE)
//        public String ask(@RequestParam String prompt);

        @PostMapping(value = "/ask", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
        public String ask(@RequestBody Map<String, Object> body);
        //{
         //       String prompt = body.get("prompt");
              //  return aiCustomService.generateResponse(prompt);
      //  }
}


