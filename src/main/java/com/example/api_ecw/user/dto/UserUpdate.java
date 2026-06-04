package com.example.api_ecw.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UserUpdate(
        @Schema(description = "Atualiza o nome do usuário", example = "Pedro Atualizado")
        @NotBlank(message = "Name is required")
        @Size(max= 255, message = "Name must be less than 255 characters")
        String name,

        @Schema(description = "Atualiza o e-mail do usuário", example = "new.email@gmail.com")
        @NotBlank(message = "Email is required")
        @Size(max= 255, message = "Email must be less than 255 characters")
        String email,

        @Schema(description = "Atualiza a senha do usuário", example = "newPassword123")
        String passwordHash,

        @Schema(description = "Atualiza a data de nascimento do usuário", example = "1990-01-01")
        LocalDate dateBirth
) {}
