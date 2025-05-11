package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "mouvement")
public class Mouvement implements Serializable {

    private  static  final long serialVersionUID=1L;
    @Id
    private String id;
    @Column(columnDefinition = "TEXT")
    private String content;
}
