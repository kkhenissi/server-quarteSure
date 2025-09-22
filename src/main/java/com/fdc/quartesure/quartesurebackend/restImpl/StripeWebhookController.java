package com.fdc.quartesure.quartesurebackend.restImpl;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/webhook")
    public String handleStripeEvent(HttpServletRequest request, @RequestBody String payload,
                                    @RequestHeader("Stripe-Signature") String sigHeader) throws IOException {
        Event event;

        try {
            // Vérification de la signature Stripe
            event = Webhook.constructEvent(
                    payload, sigHeader, endpointSecret
            );
        } catch (SignatureVerificationException e) {
            // Signature invalide → possible fraude
            return "";
        }

        // Identifier le type d’événement
        switch (event.getType()) {
            case "checkout.session.completed":
                Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
                if (session != null) {
                    String paymentIntentId = session.getPaymentIntent();
                    System.out.println("✅ Paiement réussi, PaymentIntent ID: " + paymentIntentId);

                    // Ici : sauvegarder en BDD, envoyer un email, mettre à jour une commande, etc.
                }
                break;

            case "payment_intent.succeeded":
                PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer().getObject().orElse(null);
                if (intent != null) {
                    System.out.println("✅ PaymentIntent direct: " + intent.getId());
                }
                break;

            default:
                System.out.println("ℹ️ Event non géré: " + event.getType());
        }

        return "";
    }
}
