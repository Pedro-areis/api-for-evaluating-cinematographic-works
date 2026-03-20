package com.example.api_ecw.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// DTO for User request
public record UserRequest(
        @NotBlank(message = "Name is required")
        @Size(max= 255, message = "Name must be less than 255 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Size(max= 255, message = "Email must be less than 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(max= 255, message = "Password must be less than 255 characters")
        String passwordHash,

        @NotNull(message = "Date of birth is required")
        LocalDate dateBirth
) {}
