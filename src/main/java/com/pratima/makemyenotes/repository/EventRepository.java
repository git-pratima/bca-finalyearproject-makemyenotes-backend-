package com.pratima.makemyenotes.repository;

import com.pratima.makemyenotes.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByUserId(Long id);

    List<Event> findByUserIdAndStartDateAfter(Long userId, LocalDate date);

}
