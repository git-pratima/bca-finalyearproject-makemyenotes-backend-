package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("""
        SELECT FUNCTION('DATE', q.createdDate), COUNT(q)
        FROM Question q
        WHERE MONTH(q.createdDate) = :month
          AND YEAR(q.createdDate) = :year
        GROUP BY FUNCTION('DATE', q.createdDate)
    """)
    List<Object[]> getDailyQuestionCount(
            @Param("month") int month,
            @Param("year") int year
    );

    // Next: smallest id greater than current
    @Query("SELECT q FROM Question q WHERE q.id > :currentId ORDER BY q.id ASC LIMIT 1")
    Optional<Question> findNextQuestion(@Param("currentId") Long currentId);

    // Previous: largest id less than current
    @Query("SELECT q FROM Question q WHERE q.id < :currentId ORDER BY q.id DESC LIMIT 1")
    Optional<Question> findPreviousQuestion(@Param("currentId") Long currentId);

    @Modifying
    @Transactional
    @Query("UPDATE Question q SET q.bgColor = :bgColor, q.textColor = :textColor WHERE q.id = :id")
    void updateTheme(@Param("id") Long id,
                     @Param("bgColor") String bgColor,
                     @Param("textColor") String textColor);

    List<Question> findByCategoryAndSubCategoryAndTopicAndUserId(String strCategory, String strSubcategory, String strTopic,Long userId);
}
