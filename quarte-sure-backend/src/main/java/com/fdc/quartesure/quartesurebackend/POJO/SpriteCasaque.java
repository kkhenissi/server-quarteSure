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
public class SpriteCasaque implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spriteId")
    private Integer spriteId; // "R1C4-25042024"
    private int heightSize; //:40,
    private String url; // :"https://www.pmu.fr/back-assets/hippique/casaques/19052024/R7/C4/sprite_casaques_40px_size.png",
    private Boolean originalSize; //:false
    @ManyToOne
    @JoinColumn(name="courseId", nullable=false)
    private Course course;
}
