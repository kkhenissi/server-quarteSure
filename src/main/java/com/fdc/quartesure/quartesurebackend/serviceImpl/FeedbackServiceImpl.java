package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.POJO.Feedback;
import com.fdc.quartesure.quartesurebackend.dao.FeedbackDao;
import com.fdc.quartesure.quartesurebackend.service.FeedbackService;
import com.fdc.quartesure.quartesurebackend.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackDao feedbackDao;
    private final FileStorageService fileService;

    public FeedbackServiceImpl(FeedbackDao feedbackDao, FileStorageService fileService) {
        this.feedbackDao = feedbackDao;
        this.fileService = fileService;
    }
    @Override
    public Feedback saveFeedback(String type, String description, MultipartFile screenshot) {

        Feedback fb = new Feedback();
        fb.setType(type);
        fb.setDescription(description);

        if (screenshot != null) {
            String path = fileService.saveFile(screenshot);
            fb.setScreenshotPath(path);
        }

        return feedbackDao.save(fb);
    }

    @Override
    public Feedback getFeedback(Long id) {
        return feedbackDao.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback non trouvé"));
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackDao.findAll();
    }
}


