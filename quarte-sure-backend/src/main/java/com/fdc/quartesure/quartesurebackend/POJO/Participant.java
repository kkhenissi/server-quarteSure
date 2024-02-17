package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    private  String numPmu; // 1,
    private String  nomCheval;  // "GAME WINNER",
    private CoursesCourue[] coursesCourues;


}
