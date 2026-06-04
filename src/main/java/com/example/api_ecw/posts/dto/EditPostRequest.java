package com.example.api_ecw.posts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Optional;

public record EditPostRequest(
        @Schema(description = "Atualiza o conteúdo do post", example = "Este é um post atualizado")
        @NotBlank(message = "Content cannot be null")
        String content,

        @Schema(description = "Atualiza a nota da obra", example = "7.5")
        Optional<BigDecimal> score
) {
}
