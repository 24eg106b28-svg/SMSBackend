package com.sms.dto;

import com.sms.entity.AttendanceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AttendanceRequest(
        @NotBlank(message = "Subject is required") String subject,
        @NotNull(message = "Date is required") LocalDate attendanceDate,
        @NotNull(message = "Status is required") AttendanceStatus status) {
}
