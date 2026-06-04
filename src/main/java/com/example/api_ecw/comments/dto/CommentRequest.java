package com.example.api_ecw.comments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @Schema(description = "Conteúdo do comentário", example = "Este é um comentário")
        @NotBlank(message = "Content cannot be blank")
        String content
) {}
