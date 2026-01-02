package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateRegister;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${brevo.sender-name}")
    private String senderName;

    private static final String BREVO_URL = "https://api.sendinblue.com/v3/smtp/email";

    /* =========================
       EMAIL CONFIRMATION
       ========================= */
    @Override
    @Async
    public void sendEmail(
            String to,
            String userName,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activationCode,
            String subject
    ) {
        try {
            String templateName =
                    emailTemplateName == null ? "confirm-email" : emailTemplateName.getName();

            String html = buildTemplate(templateName, Map.of(
                    "username", userName,
                    "confirmationUrl", confirmationUrl,
                    "activation_code", activationCode
            ));

            sendViaBrevo(to, subject, html);

        } catch (Exception e) {
            log.error("Erreur envoi email de confirmation vers {}", to, e);
        }
    }

    /* =========================
       EMAIL INVITATION
       ========================= */
    @Override
    @Async
    public void sendEmailInvitation(
            String to,
            String userName,
            EmailTemplateRegister emailTemplateRegister,
            String confirmationUrl,
            String subject
    ) {
        try {
            String templateName =
                    emailTemplateRegister == null ? "confirm-email" : emailTemplateRegister.getRegister();

            String html = buildTemplate(templateName, Map.of(
                    "username", userName,
                    "confirmationUrl", confirmationUrl
            ));

            sendViaBrevo(to, subject, html);

        } catch (Exception e) {
            log.error("Erreur envoi email invitation vers {}", to, e);
        }
    }

    /* =========================
       EMAIL PAIEMENT
       ========================= */
    @Override
    @Async
    public void sendStatusPayment(
            String to,
            String userName,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String subject
    ) {
        try {
            String templateName =
                    emailTemplateName == null ? "success_payment" : emailTemplateName.getName();

            String html = buildTemplate(templateName, Map.of(
                    "username", userName,
                    "confirmationUrl", confirmationUrl
            ));

            sendViaBrevo(to, subject, html);

        } catch (Exception e) {
            log.error("Erreur envoi email paiement vers {}", to, e);
        }
    }

    /* =========================
       OUTILS
       ========================= */
    private String buildTemplate(String templateName, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(templateName, context);
    }

    private void sendViaBrevo(String to, String subject, String htmlContent) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of(
                "email", senderEmail,
                "name", senderName
        ));
        body.put("to", List.of(Map.of("email", to)));
        body.put("subject", subject);
        body.put("htmlContent", htmlContent);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        restTemplate.postForObject(BREVO_URL, request, String.class);

        log.info("Email envoyé avec succès à {}", to);
    }
}
