package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateRegister;
import com.fdc.quartesure.quartesurebackend.POJO.Fillol;
import com.fdc.quartesure.quartesurebackend.POJO.User;
import com.fdc.quartesure.quartesurebackend.dao.FillolDao;
import com.fdc.quartesure.quartesurebackend.dao.UserDao;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class InvitationController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private FillolDao fillolDao;

    @Autowired
    private UserDao userDao;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${spring.application.mailing.frontend.registration-url}")
    private String registrationUrl;

    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(@RequestBody Map<String, String> payload) throws MessagingException {
        String emailInvite = payload.get("emailInvite");
        String emailParrain = payload.get("emailParrain");
        String registrationLink = registrationUrl;
        log.info("What is in emailParrain ==> {}",emailParrain);
        log.info("What is in emailInvite ==> {}",emailInvite);
    //    String registrationLink = "https://winner-app.com/#/register?emailInvite="
    //            + URLEncoder.encode(emailInvite, StandardCharsets.UTF_8);
//        +
//                "&emailParrain=" + URLEncoder.encode(emailParrain, StandardCharsets.UTF_8

        String subject = "Invitation à rejoindre WINNER";
        String content = "Bonjour,\n\n" +
                "Votre ami(e)  vous invite à rejoindre notre application WINNER.\n" +
                "Cliquez sur le lien suivant pour créer votre compte :\n" +
                registrationLink + "\n\nÀ bientôt !";
        User user = userDao.findByEmail(emailParrain);
        if(user!=null) {
            emailService.sendEmailInvitation(emailInvite,
                    emailParrain,
                    EmailTemplateRegister.REGISTER_ACCOUNT,
                    registrationLink,
                    subject);
            Fillol fillol = new Fillol();
            fillol.setEmail(emailInvite);
            fillol.setStatus("en attante de validation");
            fillol.setUser(user);
            fillolDao.save(fillol);

        }

        return ResponseEntity.ok().build();
    }
}

