package com.pratima.makemyenotes.repository;


import com.pratima.makemyenotes.entity.FeedBackDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeedbackDetailsRepository extends JpaRepository<FeedBackDetails, Long> {

    List<FeedBackDetails> findByReadUserIdIsNullOrReadUserIdNot(Long userId);

        @Query(value = """
        SELECT DISTINCT ON (f.id) f.id,f.created_at,f.created_by,f.message,f.rating,fr.read_at,fr.user_id AS read_user_id,
            CASE WHEN fr.user_id IS NOT NULL THEN 'Yes'
                ELSE 'No'
            END AS seen
        FROM feedback f
        LEFT JOIN feedback_read fr ON fr.feedback_id = f.id
            AND fr.user_id = CAST(:userId AS bigint)
        WHERE (CAST(:rating AS integer) IS NULL OR f.rating = CAST(:rating AS integer))
        AND (CAST(:startDate AS timestamp) IS NULL OR f.created_at >= CAST(:startDate AS timestamp))
        AND (CAST(:endDate AS timestamp) IS NULL OR f.created_at <= CAST(:endDate AS timestamp))
        AND ( CAST(:seen AS text) IS NULL
            OR (
                CAST(:seen AS text) = 'Yes' AND EXISTS (
                    SELECT 1 FROM feedback_read fr2
                    WHERE fr2.feedback_id = f.id
                    AND fr2.user_id = CAST(:userId AS bigint)
                ))
            OR (CAST(:seen AS text) = 'No' AND NOT EXISTS (
                    SELECT 1 FROM feedback_read fr2
                    WHERE fr2.feedback_id = f.id
                    AND fr2.user_id = CAST(:userId AS bigint))))ORDER BY f.id""", countQuery = """
        SELECT COUNT(DISTINCT f.id)
        FROM feedback f
        WHERE (CAST(:rating AS integer) IS NULL OR f.rating = CAST(:rating AS integer))
        AND (CAST(:startDate AS timestamp) IS NULL OR f.created_at >= CAST(:startDate AS timestamp))
        AND (CAST(:endDate AS timestamp) IS NULL OR f.created_at <= CAST(:endDate AS timestamp))
        AND (CAST(:seen AS text) IS NULL
            OR ( CAST(:seen AS text) = 'Yes' AND EXISTS (
                    SELECT 1 FROM feedback_read fr2
                    WHERE fr2.feedback_id = f.id
                    AND fr2.user_id = CAST(:userId AS bigint)))
            OR (CAST(:seen AS text) = 'No' AND NOT EXISTS (
                    SELECT 1 FROM feedback_read fr2
                    WHERE fr2.feedback_id = f.id
                    AND fr2.user_id = CAST(:userId AS bigint))))""", nativeQuery = true)
    Page<FeedBackDetails> findAllFeedbackDetails(
            @Param("userId") Long userId,
            @Param("rating") Integer rating,
            @Param("seen") String seen,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            Pageable pageable
    );













}
