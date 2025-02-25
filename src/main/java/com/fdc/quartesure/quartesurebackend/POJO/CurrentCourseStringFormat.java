package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Getter
@Setter
@Builder
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
public class CurrentCourseStringFormat {
    @Id
    private String id;
    @Column(columnDefinition = "TEXT")
    private String content;
}
