package com.pratima.makemyenotes.entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@Entity
@Table(name = "tbl_interviewquestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = "parentQuestion") // avoid circular hashCode
@ToString(exclude = "parentQuestion")
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question", length = 4000)
    private String question;

    @Column(name = "answer",length = 4000)
    private String answer;

    @Column(name = "added_by")
    private String addedBy;

    private Boolean expanded;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;

    // ─── Many InterviewQuestions → One Question ───────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "parent_question_id",   // FK column in interview_questions table
            nullable = false
    )
    private Question parentQuestion;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps()
    {
        String userName = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            userName = authentication.getName();
        }
        this.modifiedDate = new Date();
        this.modifiedBy = userName;
        if(this.createdDate == null) {
            this.createdDate = new Date();
            this.createdBy = userName;
            this.addedBy = userName;
        }
    }
}
