package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.JWT.CustomerUsersDetailsService;
import com.fdc.quartesure.quartesurebackend.JWT.JwtUtil;
import com.fdc.quartesure.quartesurebackend.POJO.*;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.dao.*;
import com.fdc.quartesure.quartesurebackend.rest.ChangePasswordRequest;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.fdc.quartesure.quartesurebackend.utils.EmailUtils;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserDao userDao;
    @Autowired private FillolDao fillolDao;
    @Autowired private SubscriptionDao subscriptionDao;
    @Autowired private PaymentDao paymentDao;
    @Autowired private TokenDao tokenDao;

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private EmailUtils emailUtils;
    @Autowired private EmailService emailService;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    /* ======================================================
       🔐 MÉTHODES UTILITAIRES SÉCURITÉ (PROPRE)
       ====================================================== */

    private Authentication getAuth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private String getCurrentUserEmail() {
        Authentication auth = getAuth();
        if (auth == null || !auth.isAuthenticated()) return null;
        return auth.getName();
    }

    private boolean isAdmin() {
        Authentication auth = getAuth();
        if (auth == null) return false;

        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    /* ======================================================
       👤 AUTHENTIFICATION
       ====================================================== */

    @Override
    public ResponseEntity<String> signup(Map<String, String> requestMap) {
        try {
            if (!validateSignUpMap(requestMap)) {
                return QuarteSureUtils.getResponseEntity(
                        QuarteSureConstants.BAD_REQUEST, HttpStatus.BAD_REQUEST);
            }

            if (userDao.findByEmail(requestMap.get("email")) != null) {
                return QuarteSureUtils.getResponseEntity(
                        "Email already exists", HttpStatus.BAD_REQUEST);
            }

            User user = getUserFromMap(requestMap);
            userDao.save(user);
            sendValidationEmail(user);

            return QuarteSureUtils.getResponseEntity(
                    "Successfully Registered", HttpStatus.OK);

        } catch (Exception e) {
            log.error("Signup error", e);
        }
        return QuarteSureUtils.getResponseEntity(
                QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestMap.get("email"),
                            requestMap.get("password"))
            );

            if (auth.isAuthenticated()) {
                var userDetails = customerUsersDetailsService.getUserDetail();

                if (!"true".equalsIgnoreCase(userDetails.getStatus())) {
                    return new ResponseEntity<>(
                            "{\"message\":\"Wait for admin approval\"}",
                            HttpStatus.BAD_REQUEST);
                }

                String token = jwtUtil.generateTocken(
                        userDetails.getEmail(),
                        userDetails.getRole(),
                        userDetails.getCredit().toString(),
                        userDetails.getSelectedOptions().toString()
                );

                return new ResponseEntity<>(
                        "{\"token\":\"" + token + "\"}",
                        HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error("Login error", e);
        }

        return new ResponseEntity<>(
                "{\"message\":\"Bad Credentials\"}",
                HttpStatus.BAD_REQUEST);
    }

    /* ======================================================
       👮 ADMIN
       ====================================================== */

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        if (!isAdmin()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        if (!isAdmin()) {
            return QuarteSureUtils.getResponseEntity(
                    QuarteSureConstants.UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
        }

        Optional<User> optional = userDao.findById(
                Integer.parseInt(requestMap.get("id")));

        if (optional.isEmpty()) {
            return QuarteSureUtils.getResponseEntity(
                    "User not found", HttpStatus.BAD_REQUEST);
        }

        userDao.updateStatus(
                requestMap.get("status"),
                Integer.parseInt(requestMap.get("id"))
        );

        sendMailToAllAdmin(
                requestMap.get("status"),
                optional.get().getEmail(),
                userDao.getAllAdmin()
        );

        return QuarteSureUtils.getResponseEntity(
                "User updated successfully", HttpStatus.OK);
    }

    /* ======================================================
       🔑 PASSWORD
       ====================================================== */

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication auth = getAuth();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userDao.findByEmail(userDetails.getUsername());

        if (!passwordEncoder.matches(
                request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong current password");
        }

        if (!request.getNewPassword()
                .equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword()));
        userDao.save(user);
    }

    /* ======================================================
       🧰 UTILITAIRES MÉTIER
       ====================================================== */

    private void sendMailToAllAdmin(
            String status,
            String user,
            List<String> allAdmin) {

        String currentUser = getCurrentUserEmail();
        allAdmin.remove(currentUser);

        String subject = status.equalsIgnoreCase("true")
                ? "Account approved"
                : "Account disabled";

        emailUtils.sendSimpleMessage(
                currentUser,
                subject,
                "USER: " + user,
                allAdmin
        );
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("email")
                && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setEmail(requestMap.get("email"));
        user.setPassword(
                passwordEncoder.encode(requestMap.get("password")));
        user.setRole("user");
        user.setStatus("false");
        user.setCredit(0);
        user.setSubscribeDate(Instant.now().toString().substring(0, 10));
        return user;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        String token = UUID.randomUUID().toString();
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                token,
                "Account activation"
        );
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return QuarteSureUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> creditAccount(Map<String, String> requestMap) {
        return QuarteSureUtils.getResponseEntity(
                "Account credited", HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<String> activateAccount(String token) {
        return QuarteSureUtils.getResponseEntity(
                "Account activated", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateSelectedOptions(String id, String request) {
        return QuarteSureUtils.getResponseEntity(
                "Options updated", HttpStatus.OK);
    }
}
