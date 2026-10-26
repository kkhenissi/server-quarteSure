package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/** Avancement de la part d'un filleul pour une course, vu par son parrain. */
@Getter
@AllArgsConstructor
public class FilleulJeuStatutDto {

    private Integer filleulId;
    private Long attributionId;
    private int total;
    private int jouees;
    /** true : le filleul a cliqué « J'ai fini de jouer » (sa part est à considérer comme entièrement cochée) */
    private boolean terminee;
    /**
     * Combinaisons de la part, telles qu'envoyées. Renseignées seulement quand le jeu est terminé : le parrain
     * s'en sert pour vérifier que sa répartition affichée correspond bien à la part validée.
     */
    private List<String> combinaisons;
}
