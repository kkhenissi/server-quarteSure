package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurrentCourse {

    private String allure; // "GALOP"
    private ArrayList<Participant> participants;

}
