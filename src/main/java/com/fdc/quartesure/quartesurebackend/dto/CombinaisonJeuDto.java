package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CombinaisonJeuDto {

    private Long id;
    private String numeros;
    /** A_JOUER, JOUEE, GAGNANTE ou PERDUE */
    private String statut;
}
