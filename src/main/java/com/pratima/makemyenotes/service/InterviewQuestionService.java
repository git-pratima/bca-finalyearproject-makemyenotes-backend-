package com.pratima.makemyenotes.service;

import com.pratima.makemyenotes.dto.InterviewQuestionDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InterviewQuestionService {

    List<InterviewQuestionDTO> getByParentQuestionId(Long parentQuestionId);

    InterviewQuestionDTO getById(Long id);

    InterviewQuestionDTO save(InterviewQuestionDTO dto);

    InterviewQuestionDTO update(Long id, InterviewQuestionDTO dto);

    void delete(Long id);

    void deleteAllByParentQuestionId(Long parentQuestionId);

}
