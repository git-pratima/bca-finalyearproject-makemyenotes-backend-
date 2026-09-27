package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.FeedbackRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FeedbackReadRepository extends JpaRepository<FeedbackRead, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM FeedbackRead fr WHERE fr.feedback.id = :feedbackId AND fr.user.id = :userId")
    void deleteByFeedbackAndUser(@Param("feedbackId") Long feedbackId,
                                 @Param("userId") Long userId);


    @Query("SELECT fr FROM FeedbackRead fr " +
            "WHERE fr.feedback.id = :feedbackId " +
            "AND fr.user.id = :userId")
    FeedbackRead findByFeedbackAndUser(@Param("feedbackId") Long feedbackId,
                                       @Param("userId") Long userId);

}
