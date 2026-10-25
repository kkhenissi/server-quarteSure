package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Disponibilité du compte connecté en tant que filleul. */
@Getter
@AllArgsConstructor
public class DisponibiliteDto {

    /** false : ce compte n'est le filleul de personne (le front masque alors le bouton) */
    private boolean filleul;
    /** true : le parrain peut lui attribuer des combinaisons */
    private boolean disponible;
    /** Date ISO du dernier changement, null s'il n'y en a jamais eu */
    private String depuis;
}
