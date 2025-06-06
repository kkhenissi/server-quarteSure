package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.wrapper.UserWrapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserRest {
    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true)Map<String,String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true)Map<String,String> requestMap);

    @GetMapping(path = "/allUser")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PatchMapping(path = "/update/{id}")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);



    @GetMapping(path = "/activate")
    public ResponseEntity<String> confirm(@RequestParam(required = true) String token );
    @GetMapping(path = "/checkToken")
    public ResponseEntity<String> checkToken();

    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String,String> requestMap);

    @PatchMapping(value = "/updateSelectedOptions/{id}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> updateSelectedOptions(@RequestBody(required = true) String request);

}
