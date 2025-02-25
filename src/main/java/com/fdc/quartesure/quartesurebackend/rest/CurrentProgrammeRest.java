package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping(path = "/currentprogramme")
public interface CurrentProgrammeRest {

    @PostMapping(path = "/today")
    public Object getTodayProgramme(@RequestBody(required = true) Map<String, String> requestMap);


    @PostMapping(path = "/yesterday")
    public Object getYesterdayProgramme(@RequestBody(required = true) Map<String, String> requestMap);

}
