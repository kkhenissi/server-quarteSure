package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.CurrentProgrammeStringFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CurrentProgrammeStringFormatDao extends JpaRepository<CurrentProgrammeStringFormat, String> {
    Optional<CurrentProgrammeStringFormat> findById(@Param("id") String id);
}
