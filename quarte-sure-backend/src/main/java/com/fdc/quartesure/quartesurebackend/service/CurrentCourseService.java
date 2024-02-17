package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourse;
import com.fdc.quartesure.quartesurebackend.POJO.InfosParticipant;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface CurrentCourseService {

    ResponseEntity<CurrentCourse> getCourse(Map<String, String> requestMap);
}
