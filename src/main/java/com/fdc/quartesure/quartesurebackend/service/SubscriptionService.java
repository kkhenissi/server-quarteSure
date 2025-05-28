package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.wrapper.SubscriptionWrapper;
import org.springframework.http.ResponseEntity;

public interface SubscriptionService {

    public ResponseEntity<String> subscribe(SubscriptionWrapper subscriptionWrapper);
}
