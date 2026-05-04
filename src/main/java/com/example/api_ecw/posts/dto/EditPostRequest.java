package com.example.api_ecw.posts.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Optional;

public record EditPostRequest(
        @NotBlank(message = "Content cannot be null")
        String content,

        Optional<BigDecimal> score
) {
}
