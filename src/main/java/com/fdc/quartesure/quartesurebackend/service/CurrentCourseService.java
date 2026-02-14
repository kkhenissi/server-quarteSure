package com.fdc.quartesure.quartesurebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.LightPerfStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import com.fdc.quartesure.quartesurebackend.dao.LightPerformanceStringFormatDao;
import com.fdc.quartesure.quartesurebackend.dto.RefCourse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface CurrentCourseService {

    ResponseEntity<String> getCourse(Map<String, String> requestMap) throws IOException;

    Optional<CurrentCourseStringFormat> getCourseFromLocalDB(Map<String, String> requestMap) throws IOException;

    ResponseEntity<String> getPerfomances(Map<String, String> requestMap);

    Optional<PerformanceStringFormat> getPerformanceFromLocalDB(Map<String, String> requestMap) throws IOException;

    ResponseEntity<String> getResultatCourse(Map<String, String> requestMap);

    JsonNode getCourseByOrder(Map<String, String> requestMap, int numOrderTarget);

    ResponseEntity<String> getOrdreArriveeByNumOrdre(Map<String, String> requestMap, int numOrdreTarget);

    ResponseEntity<String> getLightPerf(Map<Object, String> requestMap);

    Optional<LightPerfStringFormat> getLightPerfFromLocalDB(Map<String, String> requestMap) throws IOException;
}
