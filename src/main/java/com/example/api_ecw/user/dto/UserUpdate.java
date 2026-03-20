package com.example.api_ecw.user.dto;

import java.time.LocalDate;

public record UserUpdate(
        String name,
        String email,
        String passwordHash,
        LocalDate dateBirth
) {}
