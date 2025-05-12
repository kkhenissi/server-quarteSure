package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.wrapper.TrafficStatsWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/traffic/stats")
public interface TrafficController {
    @PostMapping("/redirect")
    ResponseEntity redirect(@RequestBody Map<String, String> requestMap, Map<String, String> response) throws IOException;
    @GetMapping("/daily")
    List<TrafficStatsWrapper> getStatsGroupedByDate();
    @GetMapping("/weekly")
    List<TrafficStatsWrapper> getStatsGroupedByWeek();
    @GetMapping("/monthly")
    List<TrafficStatsWrapper> getStatsGroupedByMonth();

}
