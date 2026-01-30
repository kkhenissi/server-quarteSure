package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
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
    ) throws StripeException {
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
                Session session = (Session) event.getData().getObject();

                String paymentIntentId = session.getPaymentIntent();
                String sessionId = session.getId();
                String email = session.getCustomerEmail();

                if (session == null) {
                    log.warn("⚠️ Session Stripe null ou payload incomplet");
                    break;
                }

                // 🔥 RÉCUPÉRATION DES METADATA DEPUIS LE PAYMENT INTENT
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
                Map<String, String> metadata = paymentIntent.getMetadata();

                int credits = 0;
                if (session != null && session.getAmountSubtotal() != null) {
                    try {
                        credits = Math.toIntExact(session.getAmountSubtotal()/100);
                        log.info("⚠️ Crédits dans metadata: {}", metadata.get("credits"));
                                //Integer.parseInt(metadata.get("credits"));
                    } catch (NumberFormatException e) {
                        log.warn("⚠️ Crédits invalides dans metadata: {}", metadata.get("credits"));
                    }
                }

                if (email == null || credits <= 0 || paymentIntentId == null) {
                    log.warn("⚠️ Données Stripe invalides : email={}, credits={}, PI={}", email, credits, paymentIntentId);
                    break;
                }

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
                event.getDataObjectDeserializer().getObject().ifPresentOrElse(obj -> {
                    PaymentIntent intent = (PaymentIntent) obj;
                    String succeededPaymentIntentId = intent.getId(); // <- nouveau nom
                    log.info("✅ PaymentIntent succeeded : {}", succeededPaymentIntentId);
                }, () -> {
                    log.warn("Impossible de désérialiser le PaymentIntent, raw: {}", event.getData().getObject());
                });
                break;

            default:
                log.info("ℹ️ Event Stripe ignoré : {}", event.getType());
        }

        return ResponseEntity.ok("ok");
    }
}
