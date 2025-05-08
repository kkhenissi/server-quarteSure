package com.fdc.quartesure.quartesurebackend.dao;


import com.fdc.quartesure.quartesurebackend.POJO.TrafficLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficLogRepository extends JpaRepository<TrafficLog, Long> {
}
