package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DernierRapportDirect implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tb_id")
    private Integer tbId; // "R1C4-25042024"

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
