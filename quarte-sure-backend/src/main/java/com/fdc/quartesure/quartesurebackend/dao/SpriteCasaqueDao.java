package com.fdc.quartesure.quartesurebackend.dao;


import com.fdc.quartesure.quartesurebackend.POJO.GainPatricipant;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpriteCasaqueDao extends JpaRepository<GainPatricipant, Integer> {

   // String findByCourseId(@Param("courseId") String courseId);
}
