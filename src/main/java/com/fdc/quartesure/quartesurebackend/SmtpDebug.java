package com.fdc.quartesure.quartesurebackend;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.net.Socket;

@Component
public class SmtpDebug {

    private static final Logger logger = LoggerFactory.getLogger(SmtpDebug.class);

    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @PostConstruct
    public void checkSmtpConnection() {
        logger.info("[SMTP DEBUG] Host: {}, Port: {}", smtpHost, smtpPort);

        try {
            // Tester la résolution DNS
            String ip = java.net.InetAddress.getByName(smtpHost).getHostAddress();
            logger.info("[SMTP DEBUG] DNS resolved: {} -> {}", smtpHost, ip);

            // Tester la connexion au port
            Socket socket = new Socket();
            long start = System.currentTimeMillis();
            socket.connect(new InetSocketAddress(smtpHost, smtpPort), 5000); // timeout 5 sec
            long end = System.currentTimeMillis();
            socket.close();
            logger.info("[SMTP DEBUG] Connection successful! Time: {} ms", (end - start));
        } catch (Exception e) {
            logger.error("[SMTP DEBUG] Connection failed", e);
        }
    }
    @PostConstruct
    public void checkBrevoConfig() {
       // System.out.println("BREVO sender = " + senderEmail);
    }
}
