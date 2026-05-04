package com.example.api_ecw.posts.dto;

import com.example.api_ecw.enums.WorkType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

public record PostRequest(
        @NotBlank(message = "Content cannot be null")
        String content,

        @NotNull(message = "Score is required")
        BigDecimal score,

        @Enumerated(EnumType.STRING)
        @NotNull(message = "Type is required")
        WorkType type
) {}
