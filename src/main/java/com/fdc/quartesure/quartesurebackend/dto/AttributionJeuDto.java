package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/** Part de combinaisons attribuée au filleul connecté pour une course. */
@Getter
@AllArgsConstructor
public class AttributionJeuDto {

    private Long id;
    private String course;
    /** Heure de départ (epoch ms), null si le parrain ne l'a pas transmise */
    private Long departMs;
    private String parrainNom;
    /** Date ISO de l'envoi */
    private String dateEnvoi;
    /** Lignes « champ réduit », telles que reçues par e-mail */
    private List<String> tickets;
    private List<CombinaisonJeuDto> combinaisons;
}
