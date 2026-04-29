package com.example.api_ecw.comments.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank(message = "Content cannot be blank")
        String content
) {}
