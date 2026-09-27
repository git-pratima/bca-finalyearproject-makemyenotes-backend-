package com.pratima.makemyenotes.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    private String subCategory;

    private String topic;

    private String type;

    @NotBlank(message = "Name is required")
    private String question;

    @Column(length = 4000)
    private String answer;

    @Transient
    private String bookmark;

    private String level;

    @OneToMany(
            mappedBy = "parentQuestion",
            cascade = CascadeType.ALL,       // save/delete propagates
            orphanRemoval = true,            // removing from list deletes the row
            fetch = FetchType.LAZY
    )
    @JsonBackReference
    private List<InterviewQuestion> interviewQuestions = new ArrayList<>();

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private byte[] image;

    @Column(name="imageurl")
    private String imageurl;

    @Column(name = "bg_color")
    private String bgColor;

    @Column(name = "text_color")
    private String textColor;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    private String createdBy;

    private Date modifiedDate;

    private String modifiedBy;

    @Transient
    private Long pid;

    @Transient
    private String createdByCurrentUser;

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
        }
    }

}
