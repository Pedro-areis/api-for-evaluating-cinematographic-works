package com.example.api_ecw.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Schema(description = "E-mail do usuário para login", example = "example@gmail.com")
        @NotBlank(message = "Email is required")
        @Size(max= 255, message = "Email must be less than 255 characters")
        String email,

        @Schema(description = "Senha para login", example = "12345678")
        @NotBlank(message = "Password is required")
        @Size(max= 255, message = "Password must be less than 255 characters")
        String password
) {}
