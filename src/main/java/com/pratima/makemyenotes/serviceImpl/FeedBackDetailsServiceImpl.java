package com.pratima.makemyenotes.serviceImpl;

import com.pratima.makemyenotes.dto.Response;
import com.pratima.makemyenotes.entity.FeedBackDetails;
import com.pratima.makemyenotes.entity.Feedback;
import com.pratima.makemyenotes.entity.FeedbackRead;
import com.pratima.makemyenotes.repository.FeedbackDetailsRepository;
import com.pratima.makemyenotes.repository.FeedbackReadRepository;
import com.pratima.makemyenotes.repository.FeedbackRepository;
import com.pratima.makemyenotes.service.FeedbackDetailsService;
import com.pratima.makemyenotes.utility.DateTimeUtility;
import com.pratima.makemyenotes.utility.UserUtility;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedBackDetailsServiceImpl implements FeedbackDetailsService {

    @Autowired
    private FeedbackDetailsRepository feedbackDetailsRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackReadRepository feedbackReadRepository;

    @Autowired
    private UserUtility utilities;

    @Autowired
    private DateTimeUtility dateTimeUtility;

    @Override
    public List<FeedBackDetails> getUnreadFeedbackByUser(Long userId) {
        return feedbackDetailsRepository.findByReadUserIdIsNullOrReadUserIdNot(userId);
    }

    @Override
    public Response getAllFeedbackDetails(Long userId, int page, int size, String orderBy, String order, Integer rating, LocalDateTime startDate, LocalDateTime endDate, String seen) {

        Sort sort = order.equalsIgnoreCase("asc")
                ? Sort.by(orderBy).ascending()
                : Sort.by(orderBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<FeedBackDetails> page1=null;
        try{
            page1 = feedbackDetailsRepository
                    .findAllFeedbackDetails(
                            userId,
                            rating,
                            seen,
                            dateTimeUtility.convertLocalDateTimeToDate(startDate),
                            dateTimeUtility.convertLocalDateTimeToDate(endDate),
                            pageable
                    );
        } catch (Exception e) {
           e.printStackTrace();
        }

        List<FeedBackDetails> feedBackDetailsList = page1.getContent();
        return Response.builder()
                .status(200)
                .message("success")
                .feedBackDetails(feedBackDetailsList)
                .totalPages(page1.getTotalPages())
                .totalElements(page1.getTotalElements())
                .last(page1.isLast())
                .first(page1.isFirst())
                .empty(page1.isEmpty())
                .build();
    }

    @Override
    public String markFeedbackReadAndUnread(Long id) {
        String message = null;
        if(id==null){
            throw new NullPointerException("Feedback id can not be null.");
        }

        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Feedback with id " + id + " not found"));

        FeedbackRead feedbackRead = feedbackReadRepository.findByFeedbackAndUser(feedback.getId(),utilities.getLoggedInUser().getId());
        if(feedbackRead==null){
            FeedbackRead feedbackRead1 = new FeedbackRead();
            feedbackRead1.setFeedback(feedback);
            feedbackRead1.setUser(utilities.getLoggedInUser());
            feedbackReadRepository.save(feedbackRead1);
            message = "Marked as Read";
        }else{
            feedbackReadRepository.deleteByFeedbackAndUser(feedback.getId(), utilities.getLoggedInUser().getId());
            message = "Marked as Un-Read";
        }



        return message;
    }
}
