package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.JWT.AuthService;
import com.fdc.quartesure.quartesurebackend.JWT.JwtUtil;
import com.fdc.quartesure.quartesurebackend.POJO.CheckoutPayment;
import com.fdc.quartesure.quartesurebackend.POJO.EmailTemplateName;
import com.fdc.quartesure.quartesurebackend.dto.CheckoutRequest;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import com.fdc.quartesure.quartesurebackend.service.SubscriptionService;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.fdc.quartesure.quartesurebackend.wrapper.SubscriptionWrapper;
import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;


import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping(value = "/api")
@Slf4j
public class StripeController {
    // create a Gson object

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;
    private static Gson gson = new Gson();
    @Autowired
    UserService userService;

    @Autowired
    private AuthService authService;


    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtil jwtUtil;


    private long paymentchoice;
    private String authorizationHeader;

    @PostMapping("/stripe/checkout")
    public Map<String, String> checkout(
            @RequestBody CheckoutRequest req,
            @AuthenticationPrincipal UserDetails user // <- ici
    ) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        String userId = user.getUsername(); // ou user.getId() selon ta classe
        Integer amount = req.getAmount();

        String priceId = switch (amount) {
            case 20 -> "price_small";
            case 50 -> "price_premium";
            case 100 -> "price_platinum";
            default -> throw new IllegalArgumentException("Montant invalide");
        };

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://winner-front.s3-website.eu-west-3.amazonaws.com/#/success")
                        .setCancelUrl("http://winner-front.s3-website.eu-west-3.amazonaws.com/#/cancel")
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPrice(priceId)
                                        .setQuantity(1L)
                                        .build()
                        )
                        .putMetadata("userId", userId) // ✅ stocke l'ID utilisateur
                        .build();

        Session session = Session.create(params);

        return Map.of("url", session.getUrl());
    }

//    @Transactional
//    public String paymentWithCheckoutPage(HttpServletRequest request, @RequestBody CheckoutPayment payment) throws StripeException {
//        // We initilize stripe object with the api key
//        this.paymentchoice = payment.getAmount();
//        init();
//        // We create a  stripe session parameters
//        SessionCreateParams params = SessionCreateParams.builder()
//                // We will use the credit card payment method
//            //    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//            //    .setMode(SessionCreateParams.Mode.PAYMENT).setSuccessUrl(payment.getSuccessUrl())
////                .setCancelUrl(
////                        payment.getCancelUrl())
////                .addLineItem(
////                        SessionCreateParams.LineItem.builder().setQuantity(payment.getQuantity())
////                                .setPriceData(
////                                        SessionCreateParams.LineItem.PriceData.builder()
////                                                .setCurrency(payment.getCurrency()).setUnitAmount(payment.getAmount())
////                                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData
////                                                        .builder().setName(payment.getName()).build())
////                                                .build())
////                                .build())
//                .build();
//        // create a stripe session
//        Session session = Session.create(params);
//        Map<String, String> responseData = new HashMap<>();
//        // We get the sessionId and we putted inside the response data you can get more info from the session object
//        responseData.put("id", session.getId());
//        // We can return only the sessionId as a String
//        // we update credit in user entity
//
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = null;
//
//        System.out.println("€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€€");
//        System.out.println(authorizationHeader);
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//
////            token = authorizationHeader.substring(7);
////            String userName = jwtUtil.extractUserName(token);
////            Claims claims = jwtUtil.extractAllClaims(token);
//      //      log.info("What is in userName here==>{}", userName);
//    //        this.userService.creditAccount(this.paymentchoice / 100, session.getId(), userName);
//
//            return gson.toJson(responseData);
//
//        }
//
//    return null;
//    }
//    @PatchMapping(path = "/payment/{id}")
//    /**
//     * Credit user account
//     *
//     * @throws StripeException
//     */
//    @CrossOrigin(origins = "http://winner-front.s3-website.eu-west-3.amazonaws.com/")
//    public String creditUserAccount(HttpServletRequest request, @RequestBody CheckoutPayment payment) {
//        String authorizationHeader = request.getHeader("Authorization");
//        String token = null;
//        this.paymentchoice = payment.getAmount();
//        token = authorizationHeader.substring(7);
//        String userName = jwtUtil.extractUserName(token);
//        Claims claims = jwtUtil.extractAllClaims(token);
//
//         if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//
//            log.info("What is in userName here==>{}", userName);
//            log.info("What is in this.paymentchoice here==>{}", this.paymentchoice);
//            this.userService.creditAccount(this.paymentchoice, "session.getId()", userName);
//         }
//
//
//
//      //  return gson.toJson(responseData);
//
//        return new String(String.valueOf(this.paymentchoice));
//    }
//
//    @PatchMapping("/successPayment/{id}")
//
//    public String stripePaymentSuccess(@RequestBody CheckoutPayment payment) throws MessagingException {
//
//     // // //   this.userService.creditAccount(this.paymentchoice/100);
//
//     //   subscriptionWrapper.
//    //    this.subscriptionService.subscribe(())
////        String to,
////        String userName,
////        EmailTemplateName emailTemplateName,
////        String confirmationUrl,
////        //  String activationCode,
////        String subject
//        EmailTemplateName emailTemplateName = null;
//        this.emailService.sendStatusPayment("kkhenissi@yahoo.fr","moi meme", emailTemplateName,"","text a afficher ...");
//        return gson.toJson("responseData");
//
//    }
//
//    private static void init() {
//        Stripe.apiKey = "sk_test_51MaipqFzoKzk6dX5bqCUklrZxdyoqv9nxMYelFlS0QAN5u7GCphDmA7zJlzDmdwTtk1FacrT2LHJIZmA3i2zby0I00VHgJTqGt";
//    }
}
