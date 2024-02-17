package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentCourse {

    private String allure; // "GALOP"
    private Participant[] participants;
}
