package com.example.api_ecw.user.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

// DTO for User response
public record UserResponse(
        String name,
        String email,
        String passwordHash,
        LocalDate dateBirth,
        LocalDateTime createdAt
) {}
