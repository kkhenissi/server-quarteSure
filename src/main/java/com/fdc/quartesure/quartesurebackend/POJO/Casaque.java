package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Immutable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Getter
@Setter
@Builder
@ToString
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "casaque")
@Immutable
public class Casaque  {
    @Id
    String casaqueId;
    @Lob
    private byte[] casaque;
}
