package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Course;
import com.fdc.quartesure.quartesurebackend.POJO.InfosParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface InfosParticipantDao extends JpaRepository<InfosParticipant, Integer> {

  //  String findByCourseId(@Param("courseId") String courseId);
}
