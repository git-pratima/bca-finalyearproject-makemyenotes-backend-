package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.BookmarkedQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookmarkedQuestionsRepository extends JpaRepository<BookmarkedQuestions, Long> {

    Optional<BookmarkedQuestions> findByQidAndUserid(Long qid, Long userid);

    Boolean existsByQidAndUserid(Long qid, Long userid);


    void deleteAllByQid(Long id);
}
