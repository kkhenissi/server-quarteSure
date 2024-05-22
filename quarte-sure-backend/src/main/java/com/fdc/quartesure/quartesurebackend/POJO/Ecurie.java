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
public class Ecurie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ecurieId")
    private Integer ecurieId; // "R1C4-25042024"

    @ManyToOne
    @JoinColumn(name="courseId", nullable=false)
    private Course course;
}
