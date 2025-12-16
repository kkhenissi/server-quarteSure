package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "feedbacks")
@Data
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // bug, reclamation, suggestion

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String userEmail; // optionnel

    private String screenshotPath; // chemin local ou URL

    @Column(nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    private String status = "NEW"; // NEW
}