package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistanceChevalPrecedent {
      private String      libelleCourt;  //1 L 1/2",
      private String      libelleLong;  // "1 longueur 1/2",
      private String      code;  // 2,
      private String      identifiant;  // "UNE_LONGUEUR_ET_DEMIE"
}
