package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.ProcessedPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessedPaymentDao extends JpaRepository<ProcessedPayment, Long> {

    Optional<ProcessedPayment> findByPaymentIntentId(String paymentIntentId);

    boolean existsByPaymentIntentId(String paymentIntentId);
}
