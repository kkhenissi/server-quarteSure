package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
// @CrossOrigin(origins = "http://winner-front.s3-website.eu-west-3.amazonaws.com/")
public interface UserRest {

    @PostMapping(path = "/signup")
    ResponseEntity<String> signUp(@RequestBody Map<String,String> requestMap);

    @PostMapping(path = "/login")
    ResponseEntity<String> login(@RequestBody Map<String,String> requestMap);

    @GetMapping(path = "/allUser")
    ResponseEntity<List<UserWrapper>> getAllUser();

    @PatchMapping(path = "/update/{id}")
    ResponseEntity<String> update(@RequestBody Map<String,String> requestMap);

    @GetMapping(path = "/activate")
    ResponseEntity<String> confirm(@RequestParam String token );

    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @PatchMapping(path = "/changePassword")
    ResponseEntity<?> changePassword(@AuthenticationPrincipal UserDetails user, @RequestBody ChangePasswordRequest requestMap);

    @PatchMapping(value = "/updateSelectedOptions/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> updateSelectedOptions(@PathVariable String id, @RequestBody String request);

    // ===================== FORGOT PASSWORD / RESET PASSWORD =====================

    @PostMapping(path = "/forgot-password")
    ResponseEntity<String> forgotPassword(@RequestParam String email);

    @PostMapping(path = "/reset-password")
    ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String password
    );
}
