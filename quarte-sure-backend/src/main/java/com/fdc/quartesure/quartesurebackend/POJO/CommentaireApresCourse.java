package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
public class CommentaireApresCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentaireId")
    private Integer commentaireId; // "R1C4-25042024"
    private String texte;
    private String source;
}
