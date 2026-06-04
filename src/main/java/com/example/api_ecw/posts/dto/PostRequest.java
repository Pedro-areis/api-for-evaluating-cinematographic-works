package com.example.api_ecw.posts.dto;

import com.example.api_ecw.enums.WorkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Optional;

public record PostRequest(
        @Schema(description = "Conteúdo do post", example = "Este é um post")
        @NotBlank(message = "Content cannot be null")
        String content,

        @Schema(description = "Nota da obra", example = "7.5")
        BigDecimal score,

        @Schema(description = "Tipo da obra", example = "'movie' ou 'series'")
        @Enumerated(EnumType.STRING)
        @NotNull(message = "Type is required")
        WorkType type
) {}
