package com.fdc.quartesure.quartesurebackend.utils;

import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailUtils {

    private final EmailService emailService;

    /**
     * Envoi d’un message simple (admins, notifications, etc.)
     */
    public void sendSimpleMessage(
            String fromUser,
            String subject,
            String text,
            List<String> ccList
    ) {
        if (ccList == null || ccList.isEmpty()) {
            return;
        }

        for (String to : ccList) {
            try {
                emailService.sendEmail(
                        to,
                        fromUser,
                        EmailTemplateName.SIMPLE_MESSAGE,
                        "",          // pas de lien
                        text,        // message libre
                        subject
                );
            } catch (Exception e) {
                log.error("Erreur envoi email vers {}", to, e);
            }
        }
    }
}
