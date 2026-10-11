package com.sms.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record StudentRequest(
        @NotBlank(message = "Roll number is required")
        @Size(max = 30, message = "Roll number must be at most 30 characters")
        String rollNumber,

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address")
        @Size(max = 120, message = "Email must be at most 120 characters")
        String email,

        @Pattern(regexp = "^$|^[0-9+\\- ]{7,15}$", message = "Phone must be 7-15 digits")
        String phone,

        @NotBlank(message = "Department is required")
        @Size(max = 100, message = "Department must be at most 100 characters")
        String department,

        @Size(max = 100, message = "Course must be at most 100 characters")
        String course,

        @Min(value = 1, message = "Year must be between 1 and 8")
        @Max(value = 8, message = "Year must be between 1 and 8")
        Integer academicYear,

        LocalDate dateOfBirth,

        @Size(max = 255, message = "Address must be at most 255 characters")
        String address,

        @Pattern(regexp = "^$|^.{6,50}$", message = "Password must be 6-50 characters")
        String password) {
}
