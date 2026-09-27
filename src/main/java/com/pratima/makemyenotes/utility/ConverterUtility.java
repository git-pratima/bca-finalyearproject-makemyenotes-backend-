package com.pratima.makemyenotes.utility;

import com.pratima.makemyenotes.dto.CategoryDTO;
import com.pratima.makemyenotes.dto.EventDTO;
import com.pratima.makemyenotes.dto.InterviewQuestionDTO;
import com.pratima.makemyenotes.entity.*;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConverterUtility {

    @Autowired
    ModelMapper mapper;

    public InterviewQuestion interviewQuestionDTOToInterviewQuestion(InterviewQuestionDTO dto) {
        return mapper.map(dto, InterviewQuestion.class);
    }

    public InterviewQuestionDTO interviewQuestionToInterviewQuestionDTO(InterviewQuestion savedEntity) {
        return mapper.map(savedEntity, InterviewQuestionDTO.class);
    }

    public List<Question> vwQuestionListToQuestionList(List<VwQuestion> vwQuestionList){
        List<Question> questionList = vwQuestionList.stream()
                .map(vwQuestion -> mapper.map(vwQuestion, Question.class))
                .collect(Collectors.toList());
        return questionList;
    }

    public List<Question> vwTransactionListToQuestionList(List<VwTransaction> vwTransactionList){
        List<Question> questionList = vwTransactionList.stream()
                .map(vwQuestion -> mapper.map(vwQuestion, Question.class))
                .collect(Collectors.toList());
        return questionList;
    }

    @PostConstruct
    public void init() {
        mapper.addMappings(new PropertyMap<InterviewQuestion, InterviewQuestionDTO>() {
            @Override
            protected void configure() {
                using(ctx -> ((Date) ctx.getSource()).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                        .map(source.getCreatedDate(), destination.getCreatedDate());

                using(ctx -> ((Date) ctx.getSource()).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime())
                        .map(source.getModifiedDate(), destination.getModifiedDate());
            }
        });
    }

    public Category categoryDtoToCategory(CategoryDTO categoryDTO) {
        return mapper.map(categoryDTO, Category.class);
    }

    public List<CategoryDTO> categoryListToCategoryListDTO(List<Category> categoryList) {
        List<CategoryDTO> categoryDTOList = mapper.map(categoryList, new TypeToken<List<CategoryDTO>>() {}.getType());
        return categoryDTOList;
    }

    public CategoryDTO categoryToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = mapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    public Event eventDtoToEvent(EventDTO eventDTO) {
        Event event = mapper.map(eventDTO, Event.class);
        return event;
    }
}
