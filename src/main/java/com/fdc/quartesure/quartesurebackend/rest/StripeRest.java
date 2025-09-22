package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/api")
public interface StripeRest {

    @PatchMapping(path = "/payment")
  //  @CrossOrigin(origins = "http://winner-one-frontend.s3-website.eu-west-3.amazonaws.com")
 //   ResponseEntity<String> creditUserAccount(HttpServletRequest request, @RequestBody CheckoutPayment payment);
      public ResponseEntity<String> creditUserAccount(@RequestBody(required = true) Map<String, String> requestMap);
}
