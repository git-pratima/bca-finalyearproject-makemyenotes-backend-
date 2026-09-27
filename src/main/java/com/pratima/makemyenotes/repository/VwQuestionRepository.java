package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.VwQuestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VwQuestionRepository extends JpaRepository<VwQuestion, Long> {

    Page<VwQuestion> findByCategoryContainingAndSubCategoryContainingAndTypeContainingAndBookmarkContainingAndUserId(
            String category,
            String subcategory,
            String type,
            String bookmark,
            Long userId,
            Pageable pageable
    );

    Page<VwQuestion> findByCategoryContainingAndSubCategoryContainingAndTypeContainingAndBookmarkContaining(
            String category,
            String subcategory,
            String type,
            String bookmark,
            Pageable pageable
    );

    Page<VwQuestion> findByCategoryContainingAndSubCategoryContainingAndTopicContainingAndTypeContainingAndBookmarkContainingAndQuestionContaining(
            String category,
            String subcategory,
            String topic,
            String type,
            String bookmark,
            String question,
            Pageable pageable
    );

}
