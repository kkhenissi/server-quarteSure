package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Résultat de l'envoi pour un filleul. */
@Getter
@AllArgsConstructor
public class SendCombinationsResult {

    private Integer filleulId;
    private String nom;
    /**
     * SENT, FAILED (l'attribution reste visible dans le compte du filleul si seul l'e-mail a échoué),
     * REFUSED (pas un filleul du parrain connecté), SKIPPED (rien à envoyer / pas d'e-mail),
     * UNAVAILABLE (le filleul s'est déclaré indisponible)
     */
    private String status;
    private String detail;
}
