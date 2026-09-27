package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.constants.AppConstants;
import com.pratima.makemyenotes.dto.QuestionThemeDTO;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.Question;
import com.pratima.makemyenotes.service.QuestionService;
import com.pratima.makemyenotes.service.TransactionService;
import com.pratima.makemyenotes.service.UserRevisionStatusService;
import com.pratima.makemyenotes.utility.UserUtility;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserUtility utility;

    @Autowired
    private UserRevisionStatusService revisionStatusService;

    @PostMapping("/add-question")
    public ResponseEntity<Response> saveQuestion(
            @RequestParam("category") String  category,
            @RequestParam("subCategory") String  subCategory,
            @RequestParam("topic") String  topic,
            @RequestParam("type") String  type,
            @RequestParam("question") String  question,
            @RequestParam("answer") String  answer,
            @RequestParam("bookmark") String  bookmark,
            @RequestParam("level") String  level,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) throws IOException {
        Question question1 = new Question();
        question1.setCategory(category);
        question1.setType(type);
        question1.setQuestion(question);
        question1.setAnswer(answer);
        question1.setLevel(level);
        question1.setSubCategory(subCategory);
        question1.setTopic(topic);
        return ResponseEntity.ok(questionService.saveQuestion(question1, imageFile, bookmark));
    }

    @PutMapping("/update-question")
    public ResponseEntity<Response> updateQuestion(
            @RequestParam("id") Long  id,
            @RequestParam("category") String  category,
            @RequestParam("subCategory") String  subCategory,
            @RequestParam("topic") String  topic,
            @RequestParam("type") String  type,
            @RequestParam("question") String  question,
            @RequestParam("answer") String  answer,
            @RequestParam("bookmark") String  bookmark,
            @RequestParam("level") String  level,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile
    ) throws IOException {
        Question dbQuestion = questionService.getQuestionById(id);
        if(category!=null){
            dbQuestion.setCategory(category);
        }
        if(type!=null){
            dbQuestion.setType(type);
        }
        if(question!=null){
            dbQuestion.setQuestion(question);
        }
        if(answer!=null){
            dbQuestion.setAnswer(answer);
        }
        if(level!=null){
            dbQuestion.setLevel(level);
        }
        if(subCategory!=null){
            dbQuestion.setSubCategory(subCategory);
        }
        if(topic!=null) {
            dbQuestion.setTopic(topic);
        }
        return ResponseEntity.ok(questionService.updateQuestion(dbQuestion, imageFile, bookmark));
    }


    @GetMapping("/get-question/{id}")
    public ResponseEntity<Response> getQuestionById(@PathVariable Long id){
        Question question = questionService.getQuestionById(id);
        question.setCreatedByCurrentUser(question.getCreatedBy().equalsIgnoreCase(utility.getLoggedInUser().getEmail())?"Yes":"No");
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .question(question)
                .message("Question retrieved successfully.")
                .build());
    }

    @GetMapping("/get-question-details/{id}")
    public ResponseEntity<Response> getQuestionDetailsById(@PathVariable Long id){
        Question question = questionService.getQuestionById(id);
        revisionStatusService.saveRevisionStatus(id, question);
        question.setCreatedByCurrentUser(question.getCreatedBy().equalsIgnoreCase(utility.getLoggedInUser().getEmail())?"Yes":"No");
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .question(question)
                .message("Question retrieved successfully.")
                .build());
    }

    @GetMapping("/get-prev-question/{id}")
    public ResponseEntity<Response> getPrevQuestionById(@PathVariable Long id){
        Question question = questionService.getPrevQuestionById(id);
        revisionStatusService.saveRevisionStatus(question.getId(),question);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .question(question)
                .message("Question retrieved successfully.")
                .build());
    }

    @GetMapping("/get-next-question/{id}")
    public ResponseEntity<Response> getNextQuestionById(@PathVariable Long id){
        Question question = questionService.getNextQuestionById(id);
        revisionStatusService.saveRevisionStatus(question.getId(),question);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .question(question)
                .message("Question retrieved successfully.")
                .build());
    }


    @GetMapping("/all")
    public ResponseEntity<Response> getQuestions1(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE) int page,
                                                     @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                     @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String orderBy,
                                                     @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_ORDER) String order,
                                                     @RequestParam(value ="category", required = false, defaultValue = "") String category,
                                                     @RequestParam(value ="subcategory", required = false, defaultValue = "") String subcategory,
                                                     @RequestParam(value ="type", required = false, defaultValue = "") String type,
                                                     @RequestParam(value ="bookmark", required = false, defaultValue = "") String bookmark) {
        Response response = questionService.getAllUserQuestion(page,size,orderBy,order,category, subcategory, type, bookmark);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-bookmarked")
    public ResponseEntity<Response> getBookmarkedQuestions(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE) int page,
                                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String orderBy,
                                                  @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_ORDER) String order,
                                                  @RequestParam(value ="category", required = false, defaultValue = "") String category,
                                                  @RequestParam(value ="subcategory", required = false, defaultValue = "") String subcategory,
                                                  @RequestParam(value ="type", required = false, defaultValue = "") String type,
                                                  @RequestParam(value ="bookmark", required = false, defaultValue = "") String bookmark) {
        Response response = questionService.getBookmarkedQuestion(page,size,orderBy,order,category, subcategory,"", type, "","Yes", "");


        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-questions")
    public ResponseEntity<Response> getAllQuestion(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE) int page,
                                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String orderBy,
                                                  @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_ORDER) String order,
                                                  @RequestParam(value ="category", required = false, defaultValue = "") String category,
                                                  @RequestParam(value ="subcategory", required = false, defaultValue = "") String subcategory,
                                                  @RequestParam(value ="type", required = false, defaultValue = "") String type,
                                                  @RequestParam(value ="bookmark", required = false, defaultValue = "") String bookmark,
                                                  @RequestParam(value ="level", required = false, defaultValue = "") String level) {
        Response response = questionService.getAllQuestion(page,size,orderBy,order,category, subcategory, type, bookmark);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Response> deleteQuestions(@PathVariable Long id){
        questionService.deleteQuestionById(id);
        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Question deleted successfully.")
                .build());
    }

    @PatchMapping("/{id}/theme")
    public ResponseEntity<Void> saveTheme(@PathVariable Long id,
                                          @Valid @RequestBody QuestionThemeDTO dto) {
        questionService.saveTheme(id, dto);
        return ResponseEntity.noContent().build();
    }

}
