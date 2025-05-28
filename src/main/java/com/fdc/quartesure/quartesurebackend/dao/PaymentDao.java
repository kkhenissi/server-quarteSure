package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PaymentDao extends JpaRepository<Payment, String> {

    List<Payment> findById(@Param("user_id") Integer user_Id);

//    List<Payment> findBySubscriptionId(@Param("subscription_id") String subscriptionId);
//
//    Optional<Payment> findByTransactionId(@Param("transaction_id") String transactionId);
}

