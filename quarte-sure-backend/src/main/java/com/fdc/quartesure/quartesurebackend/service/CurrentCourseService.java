package com.fdc.quartesure.quartesurebackend.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CurrentCourseService {

    ResponseEntity<String> getCourse(Map<String, String> requestMap);

    ResponseEntity<String> getCourseByCoursseId(Map<String, String> requestMap);

    ResponseEntity<String> getPerfomances(Map<String, String> requestMap);
}
