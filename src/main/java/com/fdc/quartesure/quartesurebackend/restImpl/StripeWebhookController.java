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
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElse(null);

                if (session == null) {
                    log.warn("⚠️ Session Stripe null");
                    break;
                }

                String paymentIntentId = session.getPaymentIntent();
                String sessionId = session.getId();
                String email = session.getCustomerEmail();
                String creditsStr = session.getMetadata() != null
                        ? session.getMetadata().get("credits")
                        : null;

                int credits = creditsStr != null ? Integer.parseInt(creditsStr) : 0;

                if (email == null || credits <= 0) {
                    log.warn("⚠️ Données Stripe invalides : email={}, credits={}", email, creditsStr);
                    break;
                }

                // ✅ Idempotence PROD
                if (!userService.isPaymentProcessed(paymentIntentId)) {
                    userService.creditAccount(email, credits, paymentIntentId);

                    // Marquer paiement traité (on marque les 2 IDs)
                    userService.markPaymentProcessed(paymentIntentId);
                    userService.markPaymentProcessed(sessionId);

                    log.info("✅ Paiement traité : email={}, credits={}, PI={}",
                            email, credits, paymentIntentId);
                } else {
                    log.info("ℹ️ Paiement déjà traité : {}", paymentIntentId);
                }
                break;

            case "payment_intent.succeeded":
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                        .getObject().orElse(null);
                if (intent != null) {
                    log.info("✅ PaymentIntent succeeded : {}", intent.getId());
                }
                break;

            default:
                log.info("ℹ️ Event Stripe ignoré : {}", event.getType());
        }

        return ResponseEntity.ok("ok");
    }
}
