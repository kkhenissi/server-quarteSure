package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.dto.LightPerfRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping(path = "/currentcourse")
public interface CurrentCourseRest {


      @PostMapping(path = "/participants")
      public Object getCourse(@RequestBody(required = true) Map<String, String> requestMap);


    @PostMapping(path = "/performances")
    public Object getPerformances(@RequestBody(required = true) Map<String, String> requestMap);


//    @PostMapping(path = "/arriveOrder")
//    public Object getResultatCourse(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/arriveOrder/{numOrdre}")
    public Object getOrdreArrivee(@PathVariable int numOrdre,
                                  @RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/participants/lightperf")
    public Object getLightPerformances(@RequestBody(required = true) LightPerfRequest reques);


}


