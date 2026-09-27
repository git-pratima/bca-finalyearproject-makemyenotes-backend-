package com.pratima.makemyenotes.serviceImpl;

import com.pratima.makemyenotes.entity.Feedback;
import com.pratima.makemyenotes.repository.FeedbackRepository;
import com.pratima.makemyenotes.service.FeedBackService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedBackServiceImpl implements FeedBackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void saveFeedback(Feedback feedback) {
        feedbackRepository.save(feedback);
    }
}
