package com.fdc.quartesure.quartesurebackend.dao;


import com.fdc.quartesure.quartesurebackend.POJO.CommentaireApresCourse;
import com.fdc.quartesure.quartesurebackend.POJO.Robe;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RobeDao extends JpaRepository<Robe, Integer> {

   // String findByCourseId(@Param("courseId") String courseId);
}
