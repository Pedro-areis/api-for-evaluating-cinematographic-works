package com.example.api_ecw.posts.dto;

import jakarta.validation.constraints.NotBlank;

public record EditPostRequest(
        @NotBlank(message = "Content cannot be null")
        String content
) {
}
