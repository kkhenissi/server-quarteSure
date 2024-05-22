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
public class Robe implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "robeId")
    private Integer robeId; // "R1C4-25042024"
    private String code; // :"020",
    private String libelleCourt; //:"BAI",
    private String libelleLong; //":"BAI"
}
