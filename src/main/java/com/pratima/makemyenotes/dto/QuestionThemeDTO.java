package com.pratima.makemyenotes.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class QuestionThemeDTO {
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Invalid hex color")
    private String bgColor;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Invalid hex color")
    private String textColor;

    // getters + setters
}
