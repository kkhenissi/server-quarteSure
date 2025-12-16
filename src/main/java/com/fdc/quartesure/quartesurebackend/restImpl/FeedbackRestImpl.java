package com.fdc.quartesure.quartesurebackend.restImpl;

import com.fdc.quartesure.quartesurebackend.POJO.Feedback;
import com.fdc.quartesure.quartesurebackend.rest.FeedbackRest;
import com.fdc.quartesure.quartesurebackend.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class FeedbackRestImpl implements FeedbackRest {

    private final FeedbackService service;

    public FeedbackRestImpl(FeedbackService service) {
        this.service = service;
    }



    @Override
    public ResponseEntity<Feedback> createFeedback(
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile screenshot
    ) {
        Feedback created = service.saveFeedback(type, description, screenshot);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedback(@PathVariable Long id) {
        Feedback fb = service.getFeedback(id);
        return ResponseEntity.ok(fb);
    }

    @Override
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {

        List<Feedback> list = service.getAllFeedbacks();
        return ResponseEntity.ok(list);

    }
}
