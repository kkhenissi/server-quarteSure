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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;


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
    @Autowired
    private ProcessedPaymentDao processedPaymentDao;

    @Value("${spring.application.mailing.frontend.activation-url}")
    private String activationUrl;

    @Value("${spring.application.mailing.frontend.reset-link-url}")
    private String resetLinkUrl;
    private final ConcurrentHashMap<String, Boolean> processedPayments = new ConcurrentHashMap<>();
    private String resetLink;

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
    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generetedToken = generateActivationCode(5);
        var token = TokenTable.builder()
                .token(generetedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        log.info("What is in  token inside == {}",token.toString());
        tokenDao.save(token);
        return generetedToken;

    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());  // 0..9
            codeBuilder.append(characters.charAt(randomIndex));

        }
        return codeBuilder.toString();
    }
    @Override
    public void changePassword(ChangePasswordRequest request) {
        Authentication auth = getAuth();

        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("User not authenticated");
        }

        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        User user = userDao.findByEmail(userDetails.getUsername());
        System.out.println("***********************************");
        System.out.println(request.getCurrentPassword());
        System.out.println(user.getPassword());
        System.out.println("**************************************");
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
                && requestMap.containsKey("password")
                && requestMap.containsKey("phoneNumber")
                && requestMap.containsKey("firstName")
                && requestMap.containsKey("lastName");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        String rawPassword = requestMap.get("password");
        String hashedPassword = passwordEncoder.encode(rawPassword);
//        user.setPassword(hashedPassword);

        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setPhoneNumber(requestMap.get("phoneNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(hashedPassword); // passwordEncode.encode();
        user.setStatus("false");
        user.setCredit(15);
        user.setSelectedOptions(requestMap.get("selectedOptions"));
        user.setLastConnexion(Instant.now().toString().substring(5,10));
        user.setSubscribeDate(Instant.now().toString().substring(0,10));
        user.setRole("user");
        return user;
    }

    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
    }


    @Override
    public ResponseEntity<String> checkToken() {
//        return Jwts.parser()
//                .setSigningKey(secretKey)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();  // ou get("email")

        return QuarteSureUtils.getResponseEntity("true",HttpStatus.OK);
    }

//    @Override
//    @Transactional
//    public ResponseEntity<String> creditAccount(Map<String, String> requestMap) {
//
//        try {
//            User userObj = userDao.findByEmail(requestMap.get("email"));
//            // jwtFilter.getCurrentUser()
//            log.info("Inside changePassword {}",userObj);
//            log.info("Inside requestMap ==> {}",requestMap.get("amount"));
//            if(userObj!=null) {
//                switch (requestMap.get("amount")) {
//                    case "20":
//                        userObj.setCredit(userObj.getCredit()+Integer.valueOf(requestMap.get("amount")));
//                        log.info("Inside creditAccount case 20 {}",userObj.getCredit());
//                        break;
//                    case "50":
//                        userObj.setCredit((int) (userObj.getCredit()+(Integer.valueOf(requestMap.get("amount"))/.75)));
//                        log.info("Inside creditAccount case 50 {}",userObj.getCredit());
//                        break;
//                    case "100":
//                        userObj.setCredit((int) (userObj.getCredit()+(Integer.valueOf(requestMap.get("amount"))/.5)));
//                        log.info("Inside creditAccount case 100 {}",userObj);
//                        break;
//                    case "150":
//                        userObj.setCredit((int) (userObj.getCredit()+(Integer.valueOf(requestMap.get("amount"))/.3)));
//                        log.info("Inside creditAccount case 150 {}",userObj);
//                        break;
//                }
//                userDao.save(userObj);
//                log.info("Inside switcase {}",userObj.getCredit());
//                Fillol fillol = fillolDao.findByEmail(requestMap.get("email"));
//                if(fillol!=null) {
//                    Subscription fromFillolToParrainSubscription = subscriptionDao.findByUser_id(fillol.getUser().getUser_id());
//                    User parrain = userDao.getById(fillol.getUser().getUser_id());
//
//                    parrain.setCredit(parrain.getCredit()+Integer.valueOf(requestMap.get("amount"))/2);
//                    userDao.save(parrain);
//                    fillol.setStatus("validé");
//                    fillolDao.saveAndFlush(fillol);
//                    fromFillolToParrainSubscription.setAmountPaid(Double.valueOf(parrain.getCredit()));
//                    subscriptionDao.saveAndFlush(fromFillolToParrainSubscription);
//                    Payment payment = new Payment();
//                    //   payment.setTransactionId(id);
//                    payment.setAmount(Double.valueOf(Integer.valueOf(requestMap.get("amount"))/10));
//                    payment.setUser(userObj);
//                    payment.setCurrency("Jour(s)");
//                    payment.setPaidAt(LocalDateTime.now());
//                    payment.setSubscription(fromFillolToParrainSubscription);
//                    // payment.set
//                    paymentDao.save(payment);
//                }
//
//
//                Subscription subscription = new Subscription();
//                subscription = subscriptionDao.findByUser_id(userObj.getUser_id());
//                log.info("Inside subscription   {}",subscription);
//                if(subscription != null) {
//                    subscription.setAmountPaid(Double.valueOf(userObj.getCredit()));
//                    subscription.setUser(userObj);
//                    subscription.setPaymentMethod("Stripe");
//                    subscription.setCreatedAt(LocalDateTime.now());
//                    //   subscription.setTransactionId(id);
//                    subscriptionDao.save(subscription);
//
//                    Payment payment = new Payment();
//                    //    payment.setTransactionId(id);
//                    payment.setAmount(Double.valueOf(Integer.valueOf(requestMap.get("amount"))));
//                    payment.setUser(userObj);
//                    payment.setCurrency("Eur");
//                    payment.setPaidAt(LocalDateTime.now());
//                    payment.setSubscription(subscription);
//                    // payment.set
//                    paymentDao.save(payment);
//                    log.info("Inside creditAccount  updated user {}",userObj);
//                    return QuarteSureUtils.getResponseEntity("account Credited Successfully.",HttpStatus.OK);
//
//                } else {
//                    subscription = new Subscription();
//                    subscription.setAmountPaid(Double.valueOf(userObj.getCredit()));
//                    subscription.setUser(userObj);
//                    subscription.setPaymentMethod("Stripe");
//                    subscription.setCreatedAt(LocalDateTime.now());
//                    //    subscription.setTransactionId(id);
//                    subscriptionDao.save(subscription);
//
//                    Payment payment = new Payment();
//                    //    payment.setTransactionId(id);
//                    payment.setAmount(Double.valueOf(Integer.valueOf(requestMap.get("amount"))));
//                    payment.setUser(userObj);
//                    payment.setCurrency("Eur");
//                    payment.setPaidAt(LocalDateTime.now());
//                    payment.setSubscription(subscription);
//                    // payment.set
//                    paymentDao.save(payment);
//                    log.info("Inside creditAccount  updated user {}",userObj);
//                    return QuarteSureUtils.getResponseEntity("account Credited Successfully.",HttpStatus.OK);
//
//                }
//
//            }
//            return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
//        }catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
//
//    }

    @Override
    @Transactional
    public void creditAccount(String email, int amount,String paymentIntentId) {
        if (isPaymentProcessed(paymentIntentId)) {
            log.info("⚠️ Paiement déjà traité : {}", paymentIntentId);
            return;
        }

        try {
            User userObj = userDao.findByEmail(email);

            if (userObj == null) {
                throw new RuntimeException("User not found with email: " + email);
            }

            log.info("Inside creditAccount - email={}, amount={}", email, amount);

            // 🔥 CREDIT LOGIC
            switch (amount) {
                case 10:
                    userObj.setCredit(userObj.getCredit() + 10);
                    break;

                case 20:
                    userObj.setCredit((int) (userObj.getCredit() + (20 / 0.75)));
                    break;

                case 30:
                    userObj.setCredit((int) (userObj.getCredit() + (30 / 0.5)));
                    break;

//                case 150:
//                    userObj.setCredit((int) (userObj.getCredit() + (150 / 0.3)));
//                    break;

                default:
                    throw new IllegalArgumentException("Invalid amount: " + amount);
            }

            userDao.save(userObj);

            // 🔁 FILLEUL / PARRAIN
            Fillol fillol = fillolDao.findByEmail(email);

            if (fillol != null) {

                User parrain = userDao.getById(fillol.getUser().getUser_id());
                parrain.setCredit(parrain.getCredit() + amount / 2);
                userDao.save(parrain);

                fillol.setStatus("validé");
                fillolDao.saveAndFlush(fillol);

                Subscription parrainSubscription =
                        subscriptionDao.findByUser_id(parrain.getUser_id());

                if (parrainSubscription != null) {
                    parrainSubscription.setAmountPaid((double) parrain.getCredit());
                    subscriptionDao.saveAndFlush(parrainSubscription);
                }

                Payment fillolPayment = new Payment();
                fillolPayment.setAmount(amount / 10.0);
                fillolPayment.setUser(userObj);
                fillolPayment.setCurrency("Jour(s)");
                fillolPayment.setPaidAt(LocalDateTime.now());
                fillolPayment.setSubscription(parrainSubscription);
                paymentDao.save(fillolPayment);
            }

            // 🔁 SUBSCRIPTION UTILISATEUR
            Subscription subscription =
                    subscriptionDao.findByUser_id(userObj.getUser_id());

            if (subscription == null) {
                subscription = new Subscription();
                subscription.setUser(userObj);
                subscription.setCreatedAt(LocalDateTime.now());
                subscription.setPaymentMethod("Stripe");
            }

            subscription.setAmountPaid((double) userObj.getCredit());
            subscriptionDao.save(subscription);

            Payment payment = new Payment();
            payment.setAmount((double) amount);
            payment.setUser(userObj);
            payment.setCurrency("EUR");
            payment.setPaidAt(LocalDateTime.now());
            payment.setSubscription(subscription);
            paymentDao.save(payment);

            log.info("✅ Account credited successfully for {}", email);

        } catch (Exception ex) {
            log.error("❌ Error while crediting account for {}", email, ex);
            throw ex; // IMPORTANT pour rollback transaction
        }
    }

    @Override
    @Transactional
    public void processStripePayment(String email, int credits, String paymentIntentId) {

        if (isPaymentProcessed(paymentIntentId)) {
            log.info("⏭️ Paiement Stripe déjà traité : {}", paymentIntentId);
            return;
        }

        creditAccount(email, credits, paymentIntentId);
        markPaymentProcessed(paymentIntentId);

        log.info("✅ Paiement Stripe traité définitivement : {}", paymentIntentId);
    }


    @Override
    public void markPaymentProcessed(String paymentIntentId) {
        ProcessedPayment payment = ProcessedPayment.builder()
                .paymentIntentId(paymentIntentId)
                .processedAt(LocalDateTime.now())
                .build();

        processedPaymentDao.save(payment);
    }


    @Override
    public boolean isPaymentProcessed(String paymentIntentId) {
        return processedPaymentDao.existsByPaymentIntentId(paymentIntentId);
    }

    @Override
    @Transactional
    public ResponseEntity<String> activateAccount(String token) {

        TokenTable savedToken = tokenDao.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException(
                    "Activation token has expired, a new token has been sent"
            );
        }

        var user = userDao.findById(savedToken.getUser().getUser_id())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setStatus("true");
        userDao.save(user);

        savedToken.setValidatedAt(LocalDateTime.now());
        tokenDao.save(savedToken);

        return QuarteSureUtils.getResponseEntity(
                "Account activated successfully",
                HttpStatus.OK
        );
    }

    @Override
    public ResponseEntity<String> updateSelectedOptions(String id,String request) {

        try {
            User  user = userDao.findByEmail(id);
            ArrayList<String> selectedOption =  new ArrayList<>();
            selectedOption.add(request);
            user.setSelectedOptions(String.valueOf(selectedOption));
            userDao.save(user);
            return QuarteSureUtils.getResponseEntity("SelectedOptions  Updated Successfully.",HttpStatus.OK);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /* ======================================================
   🔑 FORGOT PASSWORD / RESET PASSWORD
   ====================================================== */

    @Override
    public ResponseEntity<String> forgotPassword(String email) {

        User user = userDao.findByEmail(email);
        if (user == null) {
            return QuarteSureUtils.getResponseEntity(
                    "If the email exists, a reset link has been sent",
                    HttpStatus.OK
            );
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        user.setResetPasswordExpiry(LocalDateTime.now().plusMinutes(30));
        userDao.save(user);

        String resetLink = resetLinkUrl;
          //"http://localhost:4600/#/reset-password?token="
           

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.RESET_PASSWORD,
                resetLinkUrl,
                token,
                "Password Reset Request"
        );

        return QuarteSureUtils.getResponseEntity(
                "If the email exists, a reset link has been sent",
                HttpStatus.OK
        );
    }



    @Override
    public ResponseEntity<String> resetPassword(String token, String newPassword) {
        Optional<User> optional = userDao.findByResetPasswordToken(token);
        if (optional.isEmpty()) {
            return QuarteSureUtils.getResponseEntity("Invalid token", HttpStatus.BAD_REQUEST);
        }

        User user = optional.get();
        if (user.getResetPasswordExpiry() == null || LocalDateTime.now().isAfter(user.getResetPasswordExpiry())) {
            return QuarteSureUtils.getResponseEntity("Token expired", HttpStatus.BAD_REQUEST);
        }

        // Mettre à jour le mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiry(null);
        userDao.save(user);

        return QuarteSureUtils.getResponseEntity(
                "Password has been successfully reset",
                HttpStatus.OK
        );
    }
}
