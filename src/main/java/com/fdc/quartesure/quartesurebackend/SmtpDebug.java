package com.fdc.quartesure.quartesurebackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SmtpDebug {

    private static final Logger log = LoggerFactory.getLogger(SmtpDebug.class);

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth:false}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable:false}")
    private boolean starttls;

    @PostConstruct
    public void logSmtpConfig() {
        log.debug("[DEBUG] SMTP host = {}", host);
        log.debug("[DEBUG] SMTP port = {}", port);
        log.debug("[DEBUG] SMTP username = {}", username);
        log.debug("[DEBUG] SMTP auth = {}", auth);
        log.debug("[DEBUG] SMTP STARTTLS = {}", starttls);
        // Ne jamais loger le password en clair en prod
        log.debug("[DEBUG] SMTP password is {} set", password != null ? "set" : "not");
    }
}

