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
}
