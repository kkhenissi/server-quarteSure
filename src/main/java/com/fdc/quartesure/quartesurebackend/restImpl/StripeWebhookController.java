package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stripe")
@Slf4j
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private UserService userService;

    /**
     * 🔔 Webhook Stripe
     */
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeEvent(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.error("⚠️ Signature Stripe invalide", e);
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        log.info("🔔 Stripe webhook reçu : {}", event.getType());

        switch (event.getType()) {

            case "checkout.session.completed":
                // Récupérer le raw object
                Map<String, Object> sessionData = (Map<String, Object>) event.getData().getObject();
                if (sessionData == null) {
                    log.warn("⚠️ Session Stripe null ou payload incomplet");
                    break;
                }

                String paymentIntentId = (String) sessionData.get("payment_intent");
                String sessionId = (String) sessionData.get("id");
                String email = (String) sessionData.get("customer_email");

                // Metadata pour les crédits
                Map<String, String> metadata = (Map<String, String>) sessionData.get("metadata");
                int credits = 0;
                if (metadata != null && metadata.get("credits") != null) {
                    try {
                        credits = Integer.parseInt(metadata.get("credits"));
                    } catch (NumberFormatException e) {
                        log.warn("⚠️ Crédits invalides dans metadata: {}", metadata.get("credits"));
                    }
                }

                if (email == null || credits <= 0 || paymentIntentId == null) {
                    log.warn("⚠️ Données Stripe invalides : email={}, credits={}, PI={}", email, credits, paymentIntentId);
                    break;
                }

                // ✅ Idempotence PROD
                if (!userService.isPaymentProcessed(paymentIntentId)) {
                    userService.creditAccount(email, credits, paymentIntentId);
                    userService.markPaymentProcessed(paymentIntentId);
                    userService.markPaymentProcessed(sessionId);

                    log.info("✅ Paiement traité : email={}, credits={}, PI={}", email, credits, paymentIntentId);
                } else {
                    log.info("ℹ️ Paiement déjà traité : {}", paymentIntentId);
                }

                break;

            case "payment_intent.succeeded":
                Map<String, Object> intentData = (Map<String, Object>) event.getData().getObject();
                String piId = intentData != null ? (String) intentData.get("id") : null;
                log.info("✅ PaymentIntent succeeded : {}", piId);
                break;

            default:
                log.info("ℹ️ Event Stripe ignoré : {}", event.getType());
        }

        return ResponseEntity.ok("ok");
    }
}
