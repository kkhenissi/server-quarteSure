package com.fdc.quartesure.quartesurebackend.dto;

import lombok.Data;

import java.util.List;

/**
 * Envoi collectif : à chaque filleul sa part de combinaisons. Le destinataire n'est JAMAIS fourni par le client :
 * on retrouve l'e-mail à partir de l'identifiant du filleul, après avoir vérifié qu'il appartient bien au parrain connecté.
 */
@Data
public class SendCombinationsRequest {

    /** Libellé de la course, ex. « R4C1 · LONGCHAMP · 14:35 » */
    private String course;
    private List<Share> shares;

    @Data
    public static class Share {
        /** Identifiant du filleul (FilleulDto.id) */
        private Integer filleulId;
        /** Tickets « champ réduit », une ligne chacun */
        private List<String> tickets;
        /** Toutes les combinaisons du filleul, une par ligne */
        private List<String> combinations;
    }
}
