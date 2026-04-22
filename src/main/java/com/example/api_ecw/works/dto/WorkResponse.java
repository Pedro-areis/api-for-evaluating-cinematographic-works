package com.example.api_ecw.works.dto;

import com.example.api_ecw.enums.WorkType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record WorkResponse(
        UUID id,
        String tilte,
        String synopsis,
        Float score,
        WorkType type,
        LocalDate releaseDate,
        LocalDateTime createdAt
) {}
