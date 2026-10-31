package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Instantané d'une course jouée par un parrain : pari, chevaux retirés, parts des filleuls, arrivée et gain.
 * Une seule ligne par parrain et par course (course_key) : le front la met à jour à chaque changement important.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "jeu_parrain", uniqueConstraints = @UniqueConstraint(name = "uk_jeu_parrain_user_course", columnNames = {"user_id", "course_key"}))
public class JeuParrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Le parrain (user.user_id) : toujours celui du JWT */
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    /** Identifiant stable de la course côté front (réunion, course, date) */
    @Column(name = "course_key", nullable = false)
    private String courseKey;

    /** Libellé de la course, ex. « R4C1 · LONGCHAMP · 14:35 » */
    @Column(name = "course")
    private String course;

    @Column(name = "depart_ms")
    private Long departMs;

    /** Libellé du pari, ex. « Multi en 5 dans le désordre » */
    @Column(name = "pari")
    private String pari;

    @Column(name = "mise", precision = 12, scale = 2)
    private BigDecimal mise;

    @Column(name = "gain", precision = 12, scale = 2)
    private BigDecimal gain;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /** JSON complet du jeu, relu tel quel par le front */
    @Column(name = "snapshot", columnDefinition = "text", nullable = false)
    private String snapshot;
}
