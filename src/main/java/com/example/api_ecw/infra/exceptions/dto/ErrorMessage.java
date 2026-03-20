package com.example.api_ecw.infra.exceptions.dto;

import java.time.Instant;

public record ErrorMessage(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {}
