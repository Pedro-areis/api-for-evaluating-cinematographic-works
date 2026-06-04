package com.example.api_ecw.scores.dto;

import com.example.api_ecw.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ScoreRequest (
        @Schema(description = "Nota da obra", example = "8.5")
        @NotNull(message = "Score is required")
        BigDecimal score,

        @Schema(description = "Tipo da obra", example = "'movie' ou 'series'")
        @Enumerated(EnumType.STRING)
        @NotNull(message = "Work type is required")
        WorkType type
) {}
