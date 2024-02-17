package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DernierRapportDirect {

    private  String typePari;  // "E_SIMPLE_GAGNANT",
    private  String rapport;  // 6.6,
    private  String typeRapport;  // "DIRECT",
    private  String indicateurTendance;  // "-",
    private  String nombreIndicateurTendance;  // -1.49,
    private  String dateRapport;  // 1707509929000,
    private  String permutation;  // 1,
    private  String favoris;  // false,
    private  String numPmu1;  // 1,
    private  String grossePrise;  // false
}
