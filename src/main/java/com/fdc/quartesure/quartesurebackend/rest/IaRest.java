package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;
@RequestMapping(path = "/iaprediction")
public interface IaRest {
    @PostMapping(path = "/predict")
    public ResponseEntity<String[]> getPrediction(@RequestBody(required = true) String requestMap) throws IOException, InterruptedException;

}
