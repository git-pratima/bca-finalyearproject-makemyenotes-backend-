package com.pratima.makemyenotes.service;

import com.pratima.makemyenotes.entity.Feedback;
import org.springframework.stereotype.Service;

@Service
public interface FeedBackService {

    void saveFeedback(Feedback feedback);

}
