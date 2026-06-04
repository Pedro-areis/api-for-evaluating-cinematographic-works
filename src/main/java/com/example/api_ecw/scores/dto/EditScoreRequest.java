package com.example.api_ecw.scores.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EditScoreRequest(
        @Schema(description = "Atualiza a sua nota da obra", example = "10")
        @NotNull(message = "Score is required")
        BigDecimal score
) {
}
