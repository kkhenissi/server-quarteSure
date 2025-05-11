package com.fdc.quartesure.quartesurebackend.dao;


import com.fdc.quartesure.quartesurebackend.POJO.TrafficLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrafficLogRepository extends JpaRepository<TrafficLog, Long> {

    @Query("SELECT t FROM TrafficLog t")
    List<TrafficLog> findAllTrafficLogs();
}
/*
    @Query("""
    SELECT new com.fdc.quartesure.quartesurebackend.wrapper.TrafficStatsWrapper(
       t.targetUrl,
       FUNCTION('DATE', t.timestamp),
       COUNT(t)
    )
    FROM TrafficLog t
    GROUP BY t.targetUrl, FUNCTION('DATE', t.timestamp)
    """)*/
