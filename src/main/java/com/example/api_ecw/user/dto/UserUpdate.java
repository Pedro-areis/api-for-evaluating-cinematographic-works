package com.example.api_ecw.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UserUpdate(
        @Schema(description = "Atualiza o nome do usuário", example = "Pedro Atualizado")
        String name,

        @Schema(description = "Atualiza o e-mail do usuário", example = "new.email@gmail.com")
        String email,

        @Schema(description = "Atualiza a senha do usuário", example = "newPassword123")
        String passwordHash,

        @Schema(description = "Atualiza a data de nascimento do usuário", example = "1990-01-01")
        LocalDate dateBirth
) {}
