package com.fdc.quartesure.quartesurebackend.dao;


import com.fdc.quartesure.quartesurebackend.POJO.CommentaireApresCourse;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentaireApresCourseDao extends JpaRepository<CommentaireApresCourse, Integer> {

   // String findByCourseId(@Param("courseId") String courseId);
}
