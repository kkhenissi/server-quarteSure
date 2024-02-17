package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.Map;

@RequestMapping(path = "/currentcourse")
public interface CurrentCourseRest {


      @GetMapping(path = "/participants")
      public ResponseEntity<CurrentCourse> getCourse(@RequestBody(required = false) Map<String,String> requestMap);
    }


