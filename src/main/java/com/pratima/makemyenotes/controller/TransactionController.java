package com.pratima.makemyenotes.controller;

import com.pratima.makemyenotes.constants.AppConstants;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.service.QuestionService;
import com.pratima.makemyenotes.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/all-transaction")
    public ResponseEntity<Response> getQuestions1(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE) int page,
                                                  @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
                                                  @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String orderBy,
                                                  @RequestParam(value = "direction", defaultValue = AppConstants.DEFAULT_ORDER) String order,
                                                  @RequestParam(value ="category", required = false, defaultValue = "") String category,
                                                  @RequestParam(value ="subcategory", required = false, defaultValue = "") String subcategory,
                                                  @RequestParam(value ="topic", required = false, defaultValue = "") String topic,
                                                  @RequestParam(value ="type", required = false, defaultValue = "") String type,
                                                  @RequestParam(value ="bookmark", required = false, defaultValue = "") String bookmark,
                                                  @RequestParam(value ="level", required = false, defaultValue = "") String level,
                                                  @RequestParam(value ="", required = false, defaultValue = "") String question) {


        Response response = transactionService.getAllTransaction(page,size,orderBy,order,category, subcategory,topic, type, level,bookmark, question);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/bookmark/{id}")
    public ResponseEntity<Response> bookmarkQuestion(@PathVariable Long id){
        Response response = transactionService.bookmarkToggleQuestion(id);
        return ResponseEntity.ok(response);
    }


}
