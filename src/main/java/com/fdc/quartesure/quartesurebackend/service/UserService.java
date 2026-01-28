package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.rest.ChangePasswordRequest;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    // Authentification et création de compte
    ResponseEntity<String> signup(Map<String, String> requestMap);
    ResponseEntity<String> login(Map<String, String> requestMap);

    // Gestion des utilisateurs
    ResponseEntity<List<UserWrapper>> getAllUser();
    ResponseEntity<String> update(Map<String, String> requestMap);
    ResponseEntity<String> updateSelectedOptions(String id, String request);
    ResponseEntity<String> activateAccount(String token);
    ResponseEntity<String> checkToken();

    // Changement de mot de passe
    void changePassword(ChangePasswordRequest request);

    // Gestion des crédits après paiement Stripe avec idempotence
    void creditAccount(String email, int credits, String paymentIntentId);

    // Idempotence : vérifier si le paiement a déjà été traité
    boolean isPaymentProcessed(String paymentIntentId);

    // ===================== FORGOT PASSWORD =====================
    ResponseEntity<String> forgotPassword(String email);
    ResponseEntity<String> resetPassword(String token, String newPassword);

    void processStripePayment(String email, int credits, String paymentIntentId);
    void markPaymentProcessed(String email, int credits, String paymentIntentId);

}
