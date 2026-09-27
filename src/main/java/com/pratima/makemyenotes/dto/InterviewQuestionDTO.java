package com.pratima.makemyenotes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InterviewQuestionDTO {
    private Long id;
    private String question;
    private String answer;
    private String addedBy;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    // Only expose the parent ID in the DTO, not the full entity
    private Long parentQuestionId;
    private String parentQuestionTitle; // optional — useful for display
    private Boolean expanded;
}
