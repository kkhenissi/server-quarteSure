package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HParticipant {

    private String numPmu;  // 2,
    private Place  place;
    private String nomCheval;  // "DONA NAIF",
    private String nomJockey;  // "BRIAN RODRIGO ENRIQUE",
    private String poidsJockey;  // 54.5,
    private String corde;  // 2,
    private DistanceAvecPrecedent distanceAvecPrecedent;  // null,
    private String itsHim;  // false,
    private String reductionKilometrique;  // null,
    private String distanceParcourue;  // null,
    private String oeillere;  // "SANS_OEILLERES"
}
