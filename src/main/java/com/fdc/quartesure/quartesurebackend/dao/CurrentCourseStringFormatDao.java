package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentCourseStringFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CurrentCourseStringFormatDao extends JpaRepository<CurrentCourseStringFormat, String> {
    Optional<CurrentCourseStringFormat> findById(@Param("id") String id);
}
