package com.pratima.makemyenotes.service;
import com.pratima.makemyenotes.dto.QuestionThemeDTO;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.Question;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public interface QuestionService {
    Response saveQuestion(Question question, MultipartFile file, String bookmark) throws IOException;

    Response updateQuestion(Question question, MultipartFile file, String bookmark) throws IOException;

    Response getAllUserQuestion(int page,int size,String orderBy,String order,String category,String subcategory,String type,String bookmark);

    Response getAllQuestion(int page,int size,String orderBy,String order,String category,String subcategory,String type,String bookmark);

    Question getQuestionById(Long id);

    Question getPrevQuestionById(Long id);

    Question getNextQuestionById(Long id);

    void deleteQuestionById(Long id);

    public Map<String, Long> getDailyQuestionCountForCurrentMonth();

    Response getBookmarkedQuestion(int page, int size, String orderBy, String order, String category, String subcategory,String topic, String type, String level,String bookmark, String question);

    void saveTheme(Long questionId, QuestionThemeDTO dto);

}
