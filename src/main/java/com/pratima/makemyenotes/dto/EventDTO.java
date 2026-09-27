package com.pratima.makemyenotes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventDTO {
    private Long id;

    private String eventName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String purpose;

    private String status;

}
