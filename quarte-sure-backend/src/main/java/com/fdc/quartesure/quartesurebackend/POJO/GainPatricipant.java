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
public class GainPatricipant implements Serializable {

           private String gainsCarriere; // 3820700,
           private String gainsVictoires;  // 2704400,
           private String gainsPlace; // 1116300,
           private String gainsAnneeEnCours;  // 26139800,
           private String gainsAnneePrecedente; // 7865945
}
