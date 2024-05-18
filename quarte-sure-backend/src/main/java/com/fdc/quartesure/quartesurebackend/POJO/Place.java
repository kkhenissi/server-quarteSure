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
public class Place implements Serializable {

    private  String place;  // 1,
    private  String rawValue;  // null,
    private  String statusArrivee;  // "PLACE"
}
