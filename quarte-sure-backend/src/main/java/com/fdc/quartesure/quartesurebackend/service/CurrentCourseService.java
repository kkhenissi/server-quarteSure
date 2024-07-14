package com.fdc.quartesure.quartesurebackend.service;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface CurrentCourseService {

    ResponseEntity<String> getCourse(Map<String, String> requestMap) throws IOException;

    ResponseEntity<String> getPerfomances(Map<String, String> requestMap);
}
