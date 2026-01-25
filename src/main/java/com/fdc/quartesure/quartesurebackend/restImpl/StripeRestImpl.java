package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.JWT.AuthService;
import com.fdc.quartesure.quartesurebackend.JWT.JwtUtil;
import com.fdc.quartesure.quartesurebackend.POJO.CheckoutPayment;
import com.fdc.quartesure.quartesurebackend.constants.QuarteSureConstants;
import com.fdc.quartesure.quartesurebackend.rest.StripeRest;
import com.fdc.quartesure.quartesurebackend.service.EmailService;
import com.fdc.quartesure.quartesurebackend.service.UserService;
import com.fdc.quartesure.quartesurebackend.utils.QuarteSureUtils;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class StripeRestImpl implements StripeRest {

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
// //   @Override
//    public ResponseEntity<String> creditUserAccount(Map<String, String> requestMap) {
//
//
//        try {
//            String authorizationHeader = request.getHeader("Authorization");
//            String token = null;
//            this.paymentchoice = payment.getAmount();
//            token = authorizationHeader.substring(7);
//            String userName = jwtUtil.extractUserName(token);
//            Claims claims = jwtUtil.extractAllClaims(token);
//
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//
////            log.info("What is in userName here==>{}", userName);
////            log.info("What is in this.paymentchoice here==>{}", this.paymentchoice);
//                this.userService.creditAccount(this.paymentchoice, "session.getId()", userName);
//                return QuarteSureUtils.getResponseEntity("User status update successfully", HttpStatus.OK);
//            }
//
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        };
//        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
//
//    }

//    @Override
//    public ResponseEntity<String> creditUserAccount(Map<String, String> requestMap) {
//
//        try {
//            return userService.creditAccount(requestMap);
//        }catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return QuarteSureUtils.getResponseEntity(QuarteSureConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
