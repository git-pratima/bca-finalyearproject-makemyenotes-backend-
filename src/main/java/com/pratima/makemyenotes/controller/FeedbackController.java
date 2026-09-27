package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.constants.AppConstants;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.Feedback;
import com.pratima.makemyenotes.repository.FeedbackRepository;
import com.pratima.makemyenotes.service.FeedBackService;
import com.pratima.makemyenotes.service.FeedbackDetailsService;
import com.pratima.makemyenotes.utility.UserUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Slf4j
public class FeedbackController {

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private UserUtility utility;

    @Autowired
    private FeedbackDetailsService feedbackDetailsService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping("/save")
    public ResponseEntity<Response> saveFeedBack(@RequestBody Feedback feedback){
        try {
            feedBackService.saveFeedback(feedback);
        }catch (Exception e){
            return ResponseEntity.ok(Response.builder()
                    .status(500)
                    .message("Feedback Submission Failed.")
                    .build());
        }
        return ResponseEntity.ok(Response.builder()
                .status(500)
                .message("Feedback Submitted.")
                .build());
    }

    @GetMapping("/get")
    public ResponseEntity<Response> getAllFeedback(){
        return null;
    }

    //it will return all the feedback which admin user not yet seen
    @GetMapping("/all-feedback")
    public ResponseEntity<Response> getAllUserUnreadFeedback(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE) int page,
                                                             @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                             @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String orderBy,
                                                             @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_ORDER) String order,
                                                             @RequestParam(value ="rating", required = false, defaultValue = "") Integer rating,
                                                             @RequestParam(value ="startOfDay", required = false, defaultValue = "")
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                 LocalDate startOfDay,
                                                             @RequestParam(value ="endOfDay", required = false, defaultValue = "")
                                                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                 LocalDate endOfDay,
                                                             @RequestParam(value = "seen", required = false) String seen){

        LocalDateTime startDateTime = startOfDay != null ? startOfDay.atStartOfDay() : null;
        LocalDateTime endDateTime = endOfDay != null ? endOfDay.atTime(LocalTime.MAX) : null;

        Response response = null;
        try {

            response =  feedbackDetailsService.getAllFeedbackDetails(utility.getLoggedInUser().getId(),
                    page,size,orderBy,order,rating,startDateTime,endDateTime,seen);

        } catch (Exception e) {
            return ResponseEntity.ok(Response.builder()
                    .status(500)
                    .message("Internal Server Error.")
                    .build());
        }
         return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete-feedback/{id}")
    public ResponseEntity<Response> deteteFeedBack(@PathVariable Long id){

        if(id!=null){
            feedbackRepository.deleteById(id);
        }

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Feedback deleted successfully")
                .build());
    }

    @GetMapping("/mark-feedback/{id}")
    public ResponseEntity<Response> markFeedbackReadUnread(@PathVariable Long id){
        String message = null;
        if(id!=null){
            message = feedbackDetailsService.markFeedbackReadAndUnread(id);
        }

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message(message)
                .build());
    }
}
