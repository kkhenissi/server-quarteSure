package com.fdc.quartesure.quartesurebackend.rest;

import com.fdc.quartesure.quartesurebackend.POJO.Feedback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/feedback")
public interface FeedbackRest {
    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<Feedback> createFeedback(String type, String description, MultipartFile screenshot);

    @GetMapping("/{id}")
    ResponseEntity<Feedback> getFeedback(Long id);

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks();

}