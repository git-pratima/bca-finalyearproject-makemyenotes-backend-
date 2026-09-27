package com.pratima.makemyenotes.serviceImpl;

import com.pratima.makemyenotes.dto.QuestionThemeDTO;
import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.BookmarkedQuestions;
import com.pratima.makemyenotes.entity.Question;
import com.pratima.makemyenotes.entity.VwQuestion;
import com.pratima.makemyenotes.entity.VwTransaction;
import com.pratima.makemyenotes.exceptions.NotFoundException;
import com.pratima.makemyenotes.repository.BookmarkedQuestionsRepository;
import com.pratima.makemyenotes.repository.QuestionRepository;
import com.pratima.makemyenotes.repository.VwQuestionRepository;
import com.pratima.makemyenotes.repository.VwTransactionRepository;
import com.pratima.makemyenotes.service.QuestionService;
import com.pratima.makemyenotes.utility.ConverterUtility;
import com.pratima.makemyenotes.utility.ImageUtility;
import com.pratima.makemyenotes.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ConverterUtility converterUtility;

    @Autowired
    private UserUtility userUtility;

    @Autowired
    private VwQuestionRepository vwQuestionRepository;

    @Autowired
    private BookmarkedQuestionsRepository bookmarkedRepository;

    @Autowired
    private VwTransactionRepository vwTransactionRepository;

    @Autowired
    private ImageUtility imageUtility;

    @Override
    public Response saveQuestion(Question question, MultipartFile file, String flgBookmark) throws IOException {

        if(file!=null && !file.isEmpty()){
           String imageUrl = imageUtility.uploadImage(file);
            question.setImageurl(imageUrl);
        }
        question.setUser(userUtility.getLoggedInUser());
        Question question1 = questionRepository.save(question);

        if(flgBookmark!=null && flgBookmark.equalsIgnoreCase("Yes")){
            BookmarkedQuestions bookmark = new BookmarkedQuestions();
            bookmark.setQid(question1.getId());
            bookmark.setUserid(userUtility.getLoggedInUser().getId());
            bookmarkedRepository.save(bookmark);
        }

        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateQuestion(Question question, MultipartFile file, String flgBookmark) throws IOException {
        if(question==null){
            throw new NullPointerException("Request Body can not be null.");
        }
        question.setUser(userUtility.getLoggedInUser());
        Question q = questionRepository.findById(question.getId())
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if(file !=null){
            if(q.getImageurl()!=null){
                imageUtility.deleteImageByUrl(q.getImageurl());
            }

            String imageUrl = imageUtility.uploadImage(file);
            question.setImageurl(imageUrl);
        }


        Question question1 = questionRepository.save(question);
        Optional<BookmarkedQuestions> bookMarked = bookmarkedRepository.findByQidAndUserid(question1.getId(), userUtility.getLoggedInUser().getId());
        if(flgBookmark!=null && flgBookmark.equalsIgnoreCase("Yes")){
            if(bookMarked.isEmpty()){
                BookmarkedQuestions bookmark = new BookmarkedQuestions();
                bookmark.setQid(question1.getId());
                bookmark.setUserid(userUtility.getLoggedInUser().getId());
                bookmarkedRepository.save(bookmark);
            }
        }else if(flgBookmark!=null && flgBookmark.equalsIgnoreCase("No")){
            if(bookMarked.isPresent()){
                bookmarkedRepository.deleteById(bookMarked.get().getId());
            }
        }
        return Response.builder()
                .status(200)
                .message("Question has been saved successfully.")
                .build();
    }

    @Override
    @Transactional
    public Response getAllUserQuestion(int page, int size, String orderBy, String order, String category, String subcategory, String type, String bookmark) {

        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VwQuestion> page1 = null;
        try {
            page1 = vwQuestionRepository
                    .findByCategoryContainingAndSubCategoryContainingAndTypeContainingAndBookmarkContainingAndUserId(
                            category != null ? category : "",
                            subcategory != null ? subcategory : "",
                            type != null ? type : "",
                            bookmark != null ? bookmark : "",
                            userUtility.getLoggedInUser().getId(),
                            pageable
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<VwQuestion> vwQuestionList = page1.getContent();
        List<Question> questionList = converterUtility.vwQuestionListToQuestionList(vwQuestionList);
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
    @Transactional
    public Response getAllQuestion(int page, int size, String orderBy, String order, String category, String subcategory, String type, String bookmark) {
        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VwQuestion> page1 = null;
        try {
            page1 = vwQuestionRepository
                    .findByCategoryContainingAndSubCategoryContainingAndTypeContainingAndBookmarkContaining(
                            category != null ? category : "",
                            subcategory != null ? subcategory : "",
                            type != null ? type : "",
                            bookmark != null ? bookmark : "",
                            pageable
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<VwQuestion> vwQuestionList = page1.getContent();
        List<Question> questionList = converterUtility.vwQuestionListToQuestionList(vwQuestionList);
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
    @Transactional
    public Question getQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(()->
                new NotFoundException("Question Not Found with id: "+id));
        Boolean bookmarked = bookmarkedRepository.existsByQidAndUserid(id, userUtility.getLoggedInUser().getId());
        if(bookmarked){
            question.setBookmark("Yes");
        }else{
            question.setBookmark("No");
        }
        return question;
    }

    @Override
    @Transactional
    public Question getPrevQuestionById(Long id) {
        Question question = questionRepository.findPreviousQuestion(id).orElseThrow(()->
                new NotFoundException("Previous Question Not Found."));
        Boolean bookmarked = bookmarkedRepository.existsByQidAndUserid(id, userUtility.getLoggedInUser().getId());
        if(bookmarked){
            question.setBookmark("Yes");
        }else{
            question.setBookmark("No");
        }
        return question;
    }

    @Override
    @Transactional
    public Question getNextQuestionById(Long id) {
        Question question = questionRepository.findNextQuestion(id).orElseThrow(()->
                new NotFoundException("Next Question Not Found."));
        Boolean bookmarked = bookmarkedRepository.existsByQidAndUserid(id, userUtility.getLoggedInUser().getId());
        if(bookmarked){
            question.setBookmark("Yes");
        }else{
            question.setBookmark("No");
        }
        return question;
    }

    @Override
    @Transactional
    public void deleteQuestionById(Long id) {
        Question question = questionRepository.findById(id).orElseThrow(()-> new NotFoundException("Question Not Found"));

        if(question.getImageurl()!=null){
            imageUtility.deleteImageByUrl(question.getImageurl());
        }
        if(question!=null){
            questionRepository.deleteById(id);
        }

        bookmarkedRepository.deleteAllByQid(id);
    }

    public Map<String, Long> getDailyQuestionCountForCurrentMonth() {

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1);

        int month = today.getMonthValue();
        int year = today.getYear();

        // 🔹 Step 1: Initialize all days with ZERO
        Map<LocalDate, Long> tempMap = new LinkedHashMap<>();

        for (LocalDate date = startOfMonth; !date.isAfter(today); date = date.plusDays(1)) {
            tempMap.put(date, 0L);
        }

        // 🔹 Step 2: Fetch actual counts from DB
        List<Object[]> results =
                questionRepository.getDailyQuestionCount(month, year);

        for (Object[] row : results) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            Long count = (Long) row[1];

            if (tempMap.containsKey(date)) {
                tempMap.put(date, count);
            }
        }

        // 🔹 Step 3: Format key as "Feb-15"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd");

        Map<String, Long> finalMap = new LinkedHashMap<>();
        tempMap.forEach((date, count) ->
                finalMap.put(date.format(formatter), count)
        );

        return finalMap;
    }

    @Override
    @Transactional
    public Response getBookmarkedQuestion(int page, int size, String orderBy, String order, String category, String subcategory, String topic, String type, String level, String bookmark, String question) {
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
    public void saveTheme(Long questionId, QuestionThemeDTO dto) {

        Question q = questionRepository.findById(questionId).get();
        if(dto!=null){
            if(dto.getBgColor()!=null && !dto.getBgColor().isEmpty()){
                q.setBgColor(dto.getBgColor());
            }
            if(dto.getTextColor()!=null && !dto.getTextColor().isEmpty()){
                q.setTextColor(dto.getTextColor());
            }
        }
        questionRepository.save(q);
    }
}
