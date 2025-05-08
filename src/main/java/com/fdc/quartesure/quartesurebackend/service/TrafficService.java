package com.fdc.quartesure.quartesurebackend.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface TrafficService {

    ResponseEntity<String> logTraffic(Map<String, String> requestMap);
}
