package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateRegister;

import javax.mail.MessagingException;

public interface EmailService {

    public void sendEmail(String to,
                          String userName,
                          EmailTemplateName emailTemplateName,
                          String confirmationUrl,
                          String activationCode,
                          String subject) throws MessagingException;
    public void sendEmailInvitation(String to,
                          String userName,
                          EmailTemplateRegister emailTemplateRegister,
                          String confirmationUrl,
      //                    String activationCode,
                          String subject) throws MessagingException;


    public void sendStatusPayment(String to,
                          String userName,
                          EmailTemplateName emailTemplateName,
                          String confirmationUrl,
                        //  String activationCode,
                          String subject) throws MessagingException;
}
