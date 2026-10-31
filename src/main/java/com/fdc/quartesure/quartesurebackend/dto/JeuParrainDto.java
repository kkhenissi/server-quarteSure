package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/** Jeu d'un parrain. Le snapshot (JSON complet) n'est renseigné que pour le détail, pas pour la liste. */
@Getter
@AllArgsConstructor
public class JeuParrainDto {

    private Long id;
    private String courseKey;
    private String course;
    private Long departMs;
    private String pari;
    private BigDecimal mise;
    private BigDecimal gain;
    /** Dates ISO */
    private String createdAt;
    private String updatedAt;
    private String snapshot;
}
