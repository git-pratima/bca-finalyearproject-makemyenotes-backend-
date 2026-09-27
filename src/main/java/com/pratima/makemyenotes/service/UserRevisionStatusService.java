package com.pratima.makemyenotes.service;

import com.pratima.makemyenotes.entity.Question;
import org.springframework.stereotype.Service;

@Service
public interface UserRevisionStatusService {
    void saveRevisionStatus(Long qid, Question question);
}
