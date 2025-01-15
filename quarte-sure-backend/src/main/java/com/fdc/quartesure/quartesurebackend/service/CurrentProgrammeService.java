package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentProgrammeStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public interface CurrentProgrammeService {
    ResponseEntity<String> getTodayProgramme(Map<String, String> requestMap) throws IOException;

    Optional<CurrentProgrammeStringFormat> getTodayProgrammeFromLocalDB(Map<String, String> requestMap) throws IOException;

    ResponseEntity<String> getYesterdayProgramme(Map<String, String> requestMap);

    Optional<PerformanceStringFormat> getYesterdayProgrammeFromLocalDB(Map<String, String> requestMap) throws IOException;
}
