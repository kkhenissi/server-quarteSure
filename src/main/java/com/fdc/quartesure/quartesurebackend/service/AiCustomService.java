package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.wrapper.CourseData;
import com.fdc.quartesure.quartesurebackend.wrapper.RegressionResult;

import java.util.List;

public interface AiCustomService {

    public String ask(String prompt);
    RegressionResult calculateRegression(List<CourseData> courses);

    List<CourseData> predictAndSort(List<CourseData> courses);
}
