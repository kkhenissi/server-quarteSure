package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString

public class Token {
    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}
