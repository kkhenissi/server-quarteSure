package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
// @CrossOrigin(origins = "http://winner-front.s3-website.eu-west-3.amazonaws.com/")
@RequestMapping(path = "/api/payByPaypal")
public interface PaypalRest {

    @PostMapping(path = "/payment")
    public ResponseEntity<String> payment(@RequestBody(required = true) Map<String,String> requestMap);
}
