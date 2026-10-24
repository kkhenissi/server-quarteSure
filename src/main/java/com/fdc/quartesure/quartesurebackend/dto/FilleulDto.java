package com.fdc.quartesure.quartesurebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Un filleul du parrain connecté. Les invités qui n'ont pas encore créé de compte n'ont que l'e-mail
 * (nom et téléphone null, inscrit = false).
 */
@Getter
@Builder
@AllArgsConstructor
public class FilleulDto {

    /** Identifiant de la ligne Fillol (celui à renvoyer pour l'envoi) */
    private Integer id;
    private String nom;
    private String telephone;
    /** Téléphone au format international (+33…), null s'il est absent ou invalide */
    private String telephoneInternational;
    private String email;
    /** Statut du parrainage : « en attente de validation » ou « validé » */
    private String status;
    /** true si le filleul a créé un compte */
    private boolean inscrit;
}
