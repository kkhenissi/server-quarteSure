package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface CurrentCourseService {

    ResponseEntity<String> getCourse(Map<String, String> requestMap) throws IOException;

    Optional<CurrentCourseStringFormat> getCourseFromLocalDB(Map<String, String> requestMap) throws IOException;

    ResponseEntity<String> getPerfomances(Map<String, String> requestMap);
}
