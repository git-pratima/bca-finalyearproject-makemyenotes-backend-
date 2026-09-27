package com.pratima.makemyenotes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pratima.makemyenotes.entity.*;
import com.pratima.makemyenotes.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    //generic
    private int status;
    private String message;

    //for login
    private String token;
    private UserRole role;
    private String expirationTime;

    //for pagination
    private Integer totalPages;
    private Long totalElements;
    private Boolean first;
    private Boolean last;
    private Boolean empty;

    //for categories
    private CategoryDTO category;
    private List<CategoryDTO> categories;
    private List<String> categoryList;
    private Map<String,String> catMap;
    private Map<String,Long> categoryCount;
    private List<String> subCategoryList;
    private List<String> topicList;

    //dashboard
    private Dashboard dashboard;

    //Interview Question
    private InterviewQuestionDTO interviewQuestionDTO;
    private List<InterviewQuestionDTO> interviewQuestionDTOList;

    //Event
    private Event event;
    private List<Event> eventList;


    //data output optional
    private UserDTO user;
    private List<UserDTO> users;

    private String userName;

    private Question question;
    private List<Question> questionList;

    private List<FeedBackDetails> feedBackDetails;

    private List<ToDo> toDoList;

    private LocalDateTime timestamp = LocalDateTime.now();

}
