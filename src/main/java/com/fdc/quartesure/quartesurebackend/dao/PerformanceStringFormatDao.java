package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.PerformanceStringFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PerformanceStringFormatDao extends JpaRepository<PerformanceStringFormat, String> {

    Optional<PerformanceStringFormat> findById(@Param("id") String id);
}
