package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.wrapper.MouvementWrapper;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface MouvementService {

    ResponseEntity<List<String>> getAll() throws IOException;

}
