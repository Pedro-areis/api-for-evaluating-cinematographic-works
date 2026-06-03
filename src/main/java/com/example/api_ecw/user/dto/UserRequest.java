package com.example.api_ecw.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// DTO for User request
public record UserRequest(
        @Schema(description = "Nome completo do usuário", example = "Pedro Augusto Reis")
        @NotBlank(message = "Name is required")
        @Size(max= 255, message = "Name must be less than 255 characters")
        String name,

        @Schema(description = "E-mail do usuário para login", example = "example@gmail.com")
        @NotBlank(message = "Email is required")
        @Size(max= 255, message = "Email must be less than 255 characters")
        String email,

        @Schema(description = "Senha para login no site", example = "12345678")
        @NotBlank(message = "Password is required")
        @Size(max= 255, message = "Password must be less than 255 characters")
        String password,

        @Schema(description = "Data de nascimento do usuário", example = "2004-08-12")
        @NotNull(message = "Date of birth is required")
        LocalDate dateBirth
) {}
