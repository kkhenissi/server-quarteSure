package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CoursesCourue {
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
