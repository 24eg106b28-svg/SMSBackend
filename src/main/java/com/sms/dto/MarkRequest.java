package com.sms.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MarkRequest(
        @NotBlank(message = "Subject is required") String subject,
        @NotBlank(message = "Exam type is required") String examType,
        @NotNull(message = "Marks obtained is required")
        @DecimalMin(value = "0", message = "Marks obtained cannot be negative") Double marksObtained,
        @NotNull(message = "Maximum marks is required")
        @DecimalMin(value = "1", message = "Maximum marks must be at least 1") Double maxMarks) {
}
