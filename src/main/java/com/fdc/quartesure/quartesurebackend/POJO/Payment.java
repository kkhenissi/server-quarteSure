package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NamedQuery (name = "Payment.findById", query = "SELECT p FROM Payment p WHERE p.user.user_id = :userId")
@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    private Double amount;

    private String currency = "EUR";

    private String transactionId;

    private String status = "succeeded";

    private LocalDateTime paidAt = LocalDateTime.now();

    // Relations
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    // Getters & Setters
}

