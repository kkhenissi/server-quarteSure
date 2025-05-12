package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.TrafficLog;
import com.fdc.quartesure.quartesurebackend.wrapper.TrafficStatsWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface TrafficService {

    ResponseEntity<String> logTraffic(Map<String, String> requestMap);

    List<TrafficLog> findAllTrafficLogs();

    List<TrafficStatsWrapper> getStatsGroupedByDate();

    List<TrafficStatsWrapper> getStatsGroupedByWeek();

    List<TrafficStatsWrapper> getStatsGroupedByMonth();
}
