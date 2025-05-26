package com.fdc.quartesure.quartesurebackend.dao;

import com.fdc.quartesure.quartesurebackend.POJO.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;


public interface SubscriptionRepository extends JpaRepository<Subscription, String> {

   List<Subscription> findByUser_id(@Param("user_Id") Integer userId);
 //  Optional<Subscription> findTopByUserIdOrderByStartDateDesc(String userId);
}
