package com.fdc.quartesure.quartesurebackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/** Enregistrement (création ou mise à jour) du jeu d'un parrain pour une course. */
@Getter
@Setter
@NoArgsConstructor
public class JeuParrainRequest {

    private String courseKey;
    private String course;
    private Long departMs;
    private String pari;
    private BigDecimal mise;
    private BigDecimal gain;
    /** JSON complet du jeu, stocké tel quel */
    private String snapshot;
}
