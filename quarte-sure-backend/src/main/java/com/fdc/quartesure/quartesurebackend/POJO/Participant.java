package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Participant {

    private  String numPmu; // 1,
    private String  nomCheval;  // "GAME WINNER",
    private CoursesCourue[] coursesCourues;


}
