package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class TrafficLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String targetUrl;
    private String ipAddress;
    private LocalDateTime timestamp;

    // Getters, setters, constructors
}
