package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DernierRapportReference implements Serializable {

    private String  typePari;  // "E_SIMPLE_GAGNANT",
    private String  rapport;  // 5.8,
    private String  typeRapport;  // "REFERENCE",
    private String  indicateurTendance;  // " ",
    private String  nombreIndicateurTendance;  // 0.0,
    private String  dateRapport;  // 1707507905000,
    private String  permutation;  // 1,
    private String  favoris;  // false,
    private String  numPmu1;  // 1,
    private String  grossePrise;  // false
}
