package com.pratima.makemyenotes.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Immutable                                          // org.hibernate.annotations.Immutable
@Subselect("SELECT * FROM vw_dashboard")        // org.hibernate.annotations.Subselect
@Synchronize("vw_dashboard")                        // org.hibernate.annotations.Synchronize
@Table(name = "vw_dashboard")
public class Dashboard {

    @Id
    private Long id;

    private Long userId;

    private Long userTotalQuestion;

    private Long userTotalBookmark;

    private Long totalQuestion;

    private Long unreadFeedback;

    private Long futureEvents;

    @Transient
    private Map<String, Long> countMap;

    @Transient
    private List<ToDo> toDoList;

    @Transient
    Map<String, Long> dailyQuestionCountMap;

    @Transient
    Map<String, Long> userMonthlyRevisionReport;

    @Transient
    List<Map<String, Object>> theorayPracticalData;

    @Transient
    Map<LocalDate, List<ToDo>> toDoMap;

    @Transient
    List<Event> futureEventList;

}
