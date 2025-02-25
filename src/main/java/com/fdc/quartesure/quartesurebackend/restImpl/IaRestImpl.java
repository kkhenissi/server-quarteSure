package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.rest.IaRest;

import com.fdc.quartesure.quartesurebackend.service.IaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
@RestController
public class IaRestImpl implements IaRest {

    public IaRestImpl(IaService iaService) {
        this.iaService = iaService;
    }

    IaService iaService;
    @Override
    public ResponseEntity<String[]> getPrediction(String requestMap) throws IOException, InterruptedException {
        System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR");
        System.out.println(requestMap);
        System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR"); // Micro services End to End Testing - ADRIA

        return iaService.getAtteletPrediction(requestMap);
    }
}
