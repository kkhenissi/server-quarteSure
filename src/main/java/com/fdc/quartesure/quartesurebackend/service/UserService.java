package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.rest.ChangePasswordRequest;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<String> signup(Map<String, String> requestMap);

    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<String> update(Map<String, String> requestMap);

    ResponseEntity<String> updateSelectedOptions(String id, String request);

    ResponseEntity<String> activateAccount(String token);

    ResponseEntity<String> checkToken();

    void changePassword(ChangePasswordRequest request);

    // ResponseEntity<String> creditAccount(Map<String, String> requestMap);

    public void creditAccount(String email, int credits);

    // ===================== FORGOT PASSWORD =====================
    ResponseEntity<String> forgotPassword(String email);

    ResponseEntity<String> resetPassword(String token, String newPassword);
}
