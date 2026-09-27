package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.InterviewQuestion;
import com.pratima.makemyenotes.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByParentQuestionOrderByCreatedDateAsc(Question parentQuestion);

    boolean existsByParentQuestion(Question parentQuestion);

    void deleteByParentQuestion(Question parentQuestion);
}
