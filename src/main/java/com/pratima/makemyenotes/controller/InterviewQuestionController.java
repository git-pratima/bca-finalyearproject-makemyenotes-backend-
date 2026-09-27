package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.dto.InterviewQuestionDTO;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.service.InterviewQuestionService;
import com.pratima.makemyenotes.utility.ConverterUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class InterviewQuestionController {

    @Autowired
    InterviewQuestionService interviewQuestionService;

    @Autowired
    ConverterUtility converterUtility;

    @PostMapping("/interviewquestion")
    public ResponseEntity<Response> saveInterviewQuestion(@RequestBody InterviewQuestionDTO request){
        String message=null;


        if(request==null){
            message = "Request Can not be null.";
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());
        }
        try {
           InterviewQuestionDTO interviewQuestionDTO = interviewQuestionService.save(request);
            return ResponseEntity.ok(Response.builder()
                    .interviewQuestionDTO(interviewQuestionDTO)
                    .status(200)
                    .message("Interview Question has been added.").build());

        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());

        }
    }

    @GetMapping("/interviewquestion/{parentQuestionId}")
    public ResponseEntity<Response> getInterviewQuestion(@PathVariable Long parentQuestionId){
        String message=null;


        if(parentQuestionId==null || parentQuestionId<0){
            message = "Parent Question Id Can not be null/negative.";
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());
        }
        try {
            List<InterviewQuestionDTO> interviewQuestionDTO = interviewQuestionService.getByParentQuestionId(parentQuestionId);
            return ResponseEntity.ok(Response.builder()
                    .interviewQuestionDTOList(interviewQuestionDTO)
                    .status(200)
                    .message("Interview Question has been Retrieved.").build());

        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());

        }
    }


    @PutMapping("/update-interviewquestion")
    public ResponseEntity<Response> updateInterviewQuestion(@RequestBody InterviewQuestionDTO request){
        String message=null;


        if(request==null){
            message = "Request Can not be null.";
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());
        }
        try {
            InterviewQuestionDTO interviewQuestionDTO = interviewQuestionService.update(request.getId(),request);
            List<InterviewQuestionDTO> interviewQuestionDTOList = interviewQuestionService.getByParentQuestionId(request.getParentQuestionId());
            return ResponseEntity.ok(Response.builder()
                    .interviewQuestionDTO(interviewQuestionDTO)
                    .status(200)
                    .message("Interview Question has been added.").build());

        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());

        }
    }

    @DeleteMapping("/delete-interviewquestion/{id}")
    public ResponseEntity<Response> deleteInterviewQuestion(@PathVariable Long id){
        String message=null;


        if(id==null){
            message = "Id Can not be null.";
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());
        }
        try {
            interviewQuestionService.delete(id);
            return ResponseEntity.ok(Response.builder()
                    .status(200)
                    .message("Interview Question has been deleted.").build());

        } catch (Exception e) {
            e.printStackTrace();
            message = e.getMessage();
            return ResponseEntity.ok(Response.builder().message(message).status(500).build());

        }
    }


}
