package com.example.api_ecw.scores.dto;

import com.example.api_ecw.enums.WorkType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ScoreRequest (
        @NotNull(message = "Score is required")
        BigDecimal score,

        @Enumerated(EnumType.STRING)
        @NotNull(message = "Work type is required")
        WorkType type
) {}
