package com.fdc.quartesure.quartesurebackend.dto;

import lombok.Data;

import java.util.List;

/** Le filleul coche (JOUEE) ou décoche (A_JOUER) des combinaisons. */
@Data
public class MarquerCombinaisonsRequest {

    private List<Long> ids;
    /** JOUEE ou A_JOUER */
    private String statut;
}
