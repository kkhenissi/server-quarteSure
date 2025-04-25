package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.rest.MouvementRest;
import com.fdc.quartesure.quartesurebackend.service.MouvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MouvementRestImpl implements MouvementRest {
    @Autowired
    MouvementService mouvementService;



    @Override
    public ResponseEntity<List<String>> getAll() {
        try {
            return mouvementService.getAll();

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<String>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
