package com.example.api_ecw.scores.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EditScoreRequest(
        @NotNull(message = "Score is required")
        BigDecimal score
) {
}
