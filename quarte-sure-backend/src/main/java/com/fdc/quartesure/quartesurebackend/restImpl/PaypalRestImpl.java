package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.rest.PaypalRest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
public class PaypalRestImpl implements PaypalRest {
    @Override
    public ResponseEntity<String> payment(Map<String, String> requestMap) {
        return null;
    }
}
