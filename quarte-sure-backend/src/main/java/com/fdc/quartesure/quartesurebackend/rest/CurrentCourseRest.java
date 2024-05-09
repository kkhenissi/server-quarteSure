package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourse;
import com.fdc.quartesure.quartesurebackend.wrapper.RefCourseWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.Map;

@RequestMapping(path = "/currentcourse")
public interface CurrentCourseRest {


      @PostMapping(path = "/participants")
      public ResponseEntity<CurrentCourse> getCourse(@RequestBody(required = true) Map<String, String> requestMap);
    }


