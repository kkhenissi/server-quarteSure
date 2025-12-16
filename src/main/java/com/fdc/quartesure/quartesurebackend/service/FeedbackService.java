package com.fdc.quartesure.quartesurebackend.service;

import com.fdc.quartesure.quartesurebackend.POJO.Feedback;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FeedbackService {

    Feedback saveFeedback(String type, String description, MultipartFile screenshot);

    Feedback getFeedback(Long id);

    List<Feedback> getAllFeedbacks();
}
