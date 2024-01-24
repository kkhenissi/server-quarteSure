package com.fdc.quartesure.quartesurebackend.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class QuarteSureUtils {

    private QuarteSureUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
    }
}
