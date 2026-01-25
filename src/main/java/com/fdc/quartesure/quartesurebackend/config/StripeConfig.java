package com.fdc.quartesure.quartesurebackend.config;

import com.stripe.Stripe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class StripeConfig {

    // 1️⃣ Essaie variable d'environnement STRIPE_SECRET_KEY, fallback sur application.properties stripe.secret.key
    @Value("${STRIPE_SECRET_KEY:${stripe.secret.key:}}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        // 2️⃣ Vérifie que la clé est présente
        if (stripeSecretKey == null || stripeSecretKey.isEmpty()) {
            throw new IllegalStateException(
                    "Stripe API key is empty! Set STRIPE_SECRET_KEY (prod) or stripe.secret.key (dev)"
            );
        }

        // 3️⃣ Initialise Stripe
        Stripe.apiKey = stripeSecretKey;

        System.out.println("Stripe API key loaded successfully");
    }
}

