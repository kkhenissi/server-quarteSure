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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class EmailServiceImpl implements EmailService {

    private final TemplateEngine templateEngine;

    @Value("${brevo.api-key}")
    private String apiKey;

    @Value("${brevo.sender-email}")
    private String senderEmail;

    @Value("${brevo.sender-name}")
    private String senderName;

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";
    public EmailServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
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
       EMAIL COMBINAISONS (filleuls)
       ========================= */

    @Override
    public boolean sendCombinations(
            String to,
            String filleulName,
            String parrainName,
            String courseLabel,
            List<String> tickets,
            List<String> combinations
    ) {
        try {
            // Tout ce qui vient du client est échappé : jamais de HTML brut dans l'e-mail
            StringBuilder html = new StringBuilder();
            html.append("<div style=\"font-family:Arial,Helvetica,sans-serif;color:#32325d;max-width:560px\">");
            html.append("<p>Bonjour ").append(esc(filleulName)).append(",</p>");
            html.append("<p>").append(esc(parrainName))
                    .append(" vous confie votre part de combinaisons pour la course <b>")
                    .append(esc(courseLabel)).append("</b>.</p>");

            if (tickets != null && !tickets.isEmpty()) {
                html.append("<h3 style=\"margin-bottom:4px\">Vos tickets</h3><ul>");
                for (String t : tickets) {
                    html.append("<li>").append(esc(t)).append("</li>");
                }
                html.append("</ul>");
            }

            int count = combinations == null ? 0 : combinations.size();
            html.append("<h3 style=\"margin-bottom:4px\">Vos ").append(count)
                    .append(" combinaison").append(count > 1 ? "s" : "").append("</h3>");
            html.append("<p style=\"line-height:1.9;font-family:monospace;font-size:15px\">");
            if (combinations != null) {
                for (String c : combinations) {
                    html.append(esc(c)).append("<br>");
                }
            }
            html.append("</p><p>Bonne chance !</p></div>");

            sendViaBrevo(to, "Vos combinaisons — " + courseLabel, html.toString());
            return true;
        } catch (Exception e) {
            log.error("Erreur envoi combinaisons vers {}", to, e);
            return false;
        }
    }

    private String esc(String value) {
        return org.springframework.web.util.HtmlUtils.htmlEscape(value == null ? "" : value);
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

       // restTemplate.postForObject(BREVO_URL, request, String.class);

        try {
            restTemplate.postForObject(BREVO_URL, request, String.class);
            log.info("Email Brevo envoyé");
        } catch (HttpClientErrorException e) {
            log.error("Code Brevo : {}", e.getStatusCode());
            log.error("Réponse Brevo : {}", e.getResponseBodyAsString());
            throw e;
        }

        log.info("Email envoyé avec succès à {}", to);
    }
}
