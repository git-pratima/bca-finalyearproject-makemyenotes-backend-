package com.pratima.makemyenotes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevisionTypeStatDto {
    private String createdDate;
    private String type;
    private Long count;
}