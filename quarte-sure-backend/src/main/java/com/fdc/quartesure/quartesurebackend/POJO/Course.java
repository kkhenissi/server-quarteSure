package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "course")
@Embeddable
@ToString
public class Course implements Serializable  {
    public static final Long serialVersionUid = 12456L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private String courseId; // "R1C4-25042024"
    @Column(name = "listOfParticipant", length = 10000)
    private ArrayList<InfosParticipant> participants;
}
