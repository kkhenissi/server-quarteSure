package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfosParticipant {


    private String       nom;
    private String       numPmu; //: 1,
    private String        age;  // 6,
    private String        sexe; // "FEMELLES",
    private String        race; // "PUR-SANG",
    private String        statut;  // "PARTANT",
    private String        placeCorde; // 1,
    private String        oeilleres; // "SANS_OEILLERES",
    private String        proprietaire;  // "DONA SERAFINA",
    private String        entraineur;  // "CARLOS ALBERTO BANI",
    private String        driver;  //TOMAS DANIEL BAEZ",
    private boolean       driverChange; // false,
    private boolean       indicateurInedit;  // false,
    private String        musique;  // "0p7p3p1p",
    private String        nombreCourses;  // 22,
    private String        nombreVictoires; // 6,
    private String        nombrePlaces;  // 13,
    private String        nombrePlacesSecond;  // 3,
    private String        nombrePlacesTroisieme; //
    private GainPatricipant gainPatricipant;
    private String        nomPere; // "CURLINELLO",
    private String        nomMere;  // "GIRLISH INTUITION",
    private String        nomPereMere;  // "ORPEN",
    private String        ordreArrivee;  // 5,
    private String        jumentPleine; /// false,
    private String        engagement;  // false,
    private String        supplement; // 0,
    private String        handicapPoids;  // 575,
    private String        poidsConditionMonteChange;  // false,
    private DistanceChevalPrecedent distanceChevalPrecedent;
    private DernierRapportDirect dernierRapportDirect;
    private DernierRapportReference dernierRapportReference;
}
