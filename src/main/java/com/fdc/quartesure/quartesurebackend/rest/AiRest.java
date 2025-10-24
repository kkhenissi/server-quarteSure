package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/ai")
public interface AiRest {

        @PostMapping(
                value = "/ask",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE // ✅ on précise que la réponse est du JSON
        )
        ResponseEntity<Object> ask(@RequestBody Map<String, Object> body);
}
