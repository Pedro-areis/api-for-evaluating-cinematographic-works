package com.example.api_ecw.posts.dto;

import com.example.api_ecw.enums.WorkType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostRequest(
        @NotBlank(message = "Content cannot be null")
        String content,

        @Enumerated(EnumType.STRING)
        @NotNull(message = "Type is required")
        WorkType type
) {}
