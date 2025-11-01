package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.wrapper.CourseData;
import com.fdc.quartesure.quartesurebackend.wrapper.RegressionResult;

import java.util.List;
import java.util.Map;

public interface AiCustomService {

  //  public String ask(String prompt);
    RegressionResult calculateRegression(List<CourseData> courses);

    public Map<String, Object> predictAndSort(List<CourseData> courses);

  //  public double calculateSpearman(List<CourseData> courses);
}
