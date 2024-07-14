package com.fdc.quartesure.quartesurebackend.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface IaService {

    ResponseEntity<String[]> getAtteletPrediction(String requestMap) throws IOException, InterruptedException;

    ResponseEntity<String[]> getPlattPrediction();

    ResponseEntity<String[]> getMontePrediction();

    ResponseEntity<String[]> getHaiPrediction();
}
