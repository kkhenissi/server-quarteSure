package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateRegister;

public interface EmailService {

    void sendEmail(
            String to,
            String userName,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String activationCode,
            String subject
    );

    void sendEmailInvitation(
            String to,
            String userName,
            EmailTemplateRegister emailTemplateRegister,
            String confirmationUrl,
            String subject
    );

    void sendStatusPayment(
            String to,
            String userName,
            EmailTemplateName emailTemplateName,
            String confirmationUrl,
            String subject
    );

    /**
     * Envoie à un filleul sa part de combinaisons. Synchrone (contrairement aux autres envois) : l'appelant a besoin
     * de savoir si l'e-mail est parti.
     *
     * @return true si l'e-mail a été accepté par Brevo
     */
    boolean sendCombinations(
            String to,
            String filleulName,
            String parrainName,
            String courseLabel,
            java.util.List<String> tickets,
            java.util.List<String> combinations
    );
}
