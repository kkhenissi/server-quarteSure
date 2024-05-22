package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class GainPatricipant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gainsParticipantId")
    private Integer gainsParticipantId; // "R1C4-25042024"
    private String gainsCarriere; // 3820700,
    private String gainsVictoires;  // 2704400,
    private String gainsPlace; // 1116300,
    private String gainsAnneeEnCours;  // 26139800,
    private String gainsAnneePrecedente; // 7865945
}
