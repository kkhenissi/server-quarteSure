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
public class DistanceChevalPrecedent implements Serializable {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "tb_id")
      private Integer tbId; // "R1C4-25042024"
      private String      libelleCourt;  //1 L 1/2",
      private String      libelleLong;  // "1 longueur 1/2",
      private String      code;  // 2,
      private String      identifiant;  // "UNE_LONGUEUR_ET_DEMIE"
}
