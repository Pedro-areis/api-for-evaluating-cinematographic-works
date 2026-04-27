package com.example.api_ecw.works.dto;

import com.example.api_ecw.enums.WorkType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record WorkResponse(
        UUID id,
        String title,
        String synopsis,
        Float score,
        WorkType type,
        List<Integer> genreIds,
        Integer tmdbId,
        LocalDate releaseDate,
        LocalDateTime createdAt
) {}
