package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface SubscriptionDao extends JpaRepository<Subscription, String> {

   Subscription findByUser_id(@Param("user_Id") Integer userId);
 //  Optional<Subscription> findTopByUserIdOrderByStartDateDesc(String userId);
}
