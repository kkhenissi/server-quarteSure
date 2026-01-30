package com.fdc.quartesure.quartesurebackend.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/mouvement")
public interface MouvementRest {

    @GetMapping(path = "/allMouvements")
    public ResponseEntity<List<String>> getAll();

   }
