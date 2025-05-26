package com.fdc.quartesure.quartesurebackend.POJO;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
// @NamedQuery(name = "User.findByEmail",query = "select u from User u where u.email =:email")
@NamedQuery (name = "Subscription.findByUser_id", query = "SELECT s FROM Subscription s WHERE s.user.user_id = :userId")
@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String plan;

    private BigDecimal amountPaid;

    private String currency = "EUR";

    private String paymentMethod;

    private String transactionId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status = "active";

    private LocalDateTime createdAt = LocalDateTime.now();

    // Relations
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "subscription", cascade = CascadeType.ALL)
    private List<Payment> payments;

    // Getters & Setters
}

