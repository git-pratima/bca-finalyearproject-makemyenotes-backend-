package com.pratima.makemyenotes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Immutable                                          // org.hibernate.annotations.Immutable
@Subselect("SELECT * FROM vw_feedback_details")        // org.hibernate.annotations.Subselect
@Synchronize("vw_feedback_details")                        // org.hibernate.annotations.Synchronize
@Table(name = "vw_feedback_details")
public class FeedBackDetails {

    @Id
    private Long id;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Kolkata")
    private Date createdAt;

    private String createdBy;

    private String message;

    private Long rating;

    private LocalDateTime readAt;

    private Long readUserId;

    private String seen;

}
