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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/stripe")
@Slf4j
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Autowired
    private UserService userService;

    @PostMapping("/webhook")
    public String handleStripeEvent(HttpServletRequest request, @RequestBody String payload,
                                    @RequestHeader("Stripe-Signature") String sigHeader) throws IOException {
        Event event;

        try {
            // Vérification de la signature Stripe
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.error("⚠️ Signature Stripe invalide", e);
            return "Signature invalide → possible fraude";
        }

        log.info("🔔 Stripe webhook reçu : event={}", event.getType());

        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer()
                        .getObject().orElse(null);

                if (session == null) {
                    log.warn("⚠️ Session Stripe null");
                    break;
                }

                String paymentIntentId = session.getPaymentIntent();
                String email = session.getCustomerEmail();
                String creditsStr = session.getMetadata() != null
                        ? session.getMetadata().get("credits")
                        : null;

                if (paymentIntentId == null || email == null || creditsStr == null) {
                    log.warn("⚠️ Données Stripe incomplètes (paymentIntent={}, email={}, credits={})",
                            paymentIntentId, email, creditsStr);
                    break;
                }

                int credits = Integer.parseInt(creditsStr);

                try {
                    userService.processStripePayment(email, credits, paymentIntentId);
                    log.info("✅ Paiement Stripe traité avec succès : {}", paymentIntentId);
                } catch (Exception ex) {
                    log.error("❌ Erreur traitement paiement Stripe {}", paymentIntentId, ex);
                    throw ex; // Stripe réessaiera → OK
                }
                break;


            case "payment_intent.succeeded":
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (intent != null) {
                    log.info("✅ PaymentIntent direct réussi : {}", intent.getId());
                }
                break;

            default:
                log.info("ℹ️ Event Stripe non géré : {}", event.getType());
        }

        return "";
    }
}