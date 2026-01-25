package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
// @CrossOrigin(origins = "http://winner-front.s3-website.eu-west-3.amazonaws.com/")
@RequestMapping(path = "/api")
public interface StripeRest {

   // @PatchMapping(path = "/payment")
  //  @CrossOrigin(origins = "http://winner-front.s3-website.eu-west-3.amazonaws.com/")
 //   ResponseEntity<String> creditUserAccount(HttpServletRequest request, @RequestBody CheckoutPayment payment);
 //     public ResponseEntity<String> creditUserAccount(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping("/payment/status/{sessionId}")
    public ResponseEntity<Map<String, Boolean>> getPaymentStatus(@PathVariable String sessionId);

}
