package com.pratima.makemyenotes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodoDTO {

    private Long id;

    private String completed;

    private String task;

    private Boolean checked;

}
