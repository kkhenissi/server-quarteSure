package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequestMapping("/api/traffic")
public interface TrafficController {
    ResponseEntity redirect(@RequestBody Map<String, String> requestMap, Map<String, String> response) throws IOException;
}
