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
public class Participant implements Serializable {

    private  String numPmu; // 1,
    private String  nomCheval;  // "GAME WINNER",
    private CoursesCourue[] coursesCourues;


}
