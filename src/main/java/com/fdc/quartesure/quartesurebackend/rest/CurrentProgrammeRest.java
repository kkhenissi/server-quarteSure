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


    /** Pénétromètre (état du terrain) d'une réunion : {"intitule":"Lourd","valeurMesure":"4,6",…} ou {} s'il n'est pas renseigné */
    @PostMapping(path = "/penetrometre")
    public Object getPenetrometre(@RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/yesterday")
    public Object getYesterdayProgramme(@RequestBody(required = true) Map<String, String> requestMap);

}
