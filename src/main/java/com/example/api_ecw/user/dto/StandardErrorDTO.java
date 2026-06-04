package com.example.api_ecw.user.dto;

public record StandardErrorDTO (
        String status,
        String error,
        String message
) {}
