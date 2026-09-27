package com.pratima.makemyenotes.serviceImpl;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.BookmarkedQuestions;
import com.pratima.makemyenotes.entity.Question;
import com.pratima.makemyenotes.entity.VwTransaction;
import com.pratima.makemyenotes.exceptions.NotFoundException;
import com.pratima.makemyenotes.repository.BookmarkedQuestionsRepository;
import com.pratima.makemyenotes.repository.QuestionRepository;
import com.pratima.makemyenotes.repository.VwTransactionRepository;
import com.pratima.makemyenotes.service.TransactionService;
import com.pratima.makemyenotes.utility.ConverterUtility;
import com.pratima.makemyenotes.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private BookmarkedQuestionsRepository bookmarkedQuestionsRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VwTransactionRepository vwTransactionRepository;

    @Autowired
    private ConverterUtility converterUtility;

    @Autowired
    private UserUtility userUtility;

    @Override
    @Transactional(readOnly = true)
    public Response getAllTransaction(int page, int size, String orderBy, String order, String category, String subcategory, String topic, String type, String level, String bookmark, String question) {

        vwTransactionRepository.setCurrentUserId(userUtility.getLoggedInUser().getId());

        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VwTransaction> page1 = null;
        try {
             page1 = vwTransactionRepository.findAllTransactions(
                    category.isEmpty()?null : category,
                    subcategory.isEmpty() ? null : subcategory,
                    topic.isEmpty()  ? null : topic,
                    type.isEmpty()  ? null : type,
                    level.isEmpty()   ? null : level,
                    bookmark.isEmpty()  ? null : bookmark,
                    question.isEmpty()    ? null : question,
                    pageable
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<VwTransaction> vwQuestionList = page1.getContent();
        List<Question> questionList = converterUtility.vwTransactionListToQuestionList(vwQuestionList);
        return Response.builder()
                .status(200)
                .message("success")
                .questionList(questionList)
                .totalPages(page1.getTotalPages())
                .totalElements(page1.getTotalElements())
                .last(page1.isLast())
                .first(page1.isFirst())
                .empty(page1.isEmpty())
                .build();
    }

    @Override
    public Response bookmarkToggleQuestion(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(()-> new NotFoundException("Question Not Found"));

        Optional<BookmarkedQuestions> bookmarkedQuestions = bookmarkedQuestionsRepository.findByQidAndUserid(question.getId(), userUtility.getLoggedInUser().getId());
        if(bookmarkedQuestions.isPresent()){
            bookmarkedQuestionsRepository.deleteById(bookmarkedQuestions.get().getId());
            return Response.builder()
                    .status(200)
                    .message("Removed Bookmarked.")
                    .build();
        }
        if(bookmarkedQuestions.isEmpty()){
            BookmarkedQuestions bookmarkQuestion = new BookmarkedQuestions();
            bookmarkQuestion.setQid(question.getId());
            bookmarkQuestion.setUserid(userUtility.getLoggedInUser().getId());
            bookmarkedQuestionsRepository.save(bookmarkQuestion);
            return Response.builder()
                    .status(200)
                    .message("Bookmarked the Question.")
                    .build();
        }

        return Response.builder()
                .status(500)
                .message("Some exception occured in toggling bookmark. Please contact Admin.")
                .build();
    }
}
