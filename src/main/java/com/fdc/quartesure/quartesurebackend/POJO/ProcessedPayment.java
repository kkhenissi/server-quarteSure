package com.fdc.quartesure.quartesurebackend.POJO;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "processed_payments",
        uniqueConstraints = @UniqueConstraint(columnNames = "payment_intent_id")
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_intent_id", nullable = false, unique = true)
    private String paymentIntentId;
    @Column(nullable = false)
    private LocalDateTime processedAt;
}
