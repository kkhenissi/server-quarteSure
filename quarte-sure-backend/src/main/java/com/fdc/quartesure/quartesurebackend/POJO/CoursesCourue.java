package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoursesCourue implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tb_id")
    private Integer tbId; // "R1C4-25042024"
    private String date;  // 1703199600000,
    private String timezoneOffset;  // 3600000,
    private String hippodrome;  // "SAN ISIDRO",
    private String nomPrix;  // "CLASICOBLUEPRIZ",
    private String discipline;  // "PLAT",
    private String allocation;  // 58968,
    private String distance;  // 2000,
    private String nbParticipants;  // 9,
    private String tempsDuPremier;  // 11976,
    private String etatTerrain;
    private HParticipant[] participants;
}
