package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.LightPerfStringFormat;
import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LightPerformanceStringFormatDao extends JpaRepository<LightPerfStringFormat, String> {
    Optional<LightPerfStringFormat> findById(@Param("id") String id);
}
