package com.pratima.makemyenotes.service;

import com.pratima.makemyenotes.dto.Response;
import org.springframework.stereotype.Service;

@Service
public interface TransactionService {

    Response getAllTransaction(int page, int size, String orderBy, String order, String category, String subcategory, String topic, String type, String level, String bookmark, String question);

    Response bookmarkToggleQuestion(Long id);
}
