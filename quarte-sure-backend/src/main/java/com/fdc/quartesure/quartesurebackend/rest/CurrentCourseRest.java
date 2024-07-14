package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/currentcourse")
public interface CurrentCourseRest {


      @PostMapping(path = "/participants")
      public Object getCourse(@RequestBody(required = true) Map<String, String> requestMap);


    @PostMapping(path = "/performances")
    public ResponseEntity<String> getPerformances(@RequestBody(required = true) Map<String, String> requestMap);

}


