package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Résultat de l'envoi pour un filleul. */
@Getter
@AllArgsConstructor
public class SendCombinationsResult {

    private Integer filleulId;
    private String nom;
    /** SENT, FAILED, REFUSED (pas un filleul du parrain connecté), SKIPPED (rien à envoyer / pas d'e-mail) */
    private String status;
    private String detail;
}
