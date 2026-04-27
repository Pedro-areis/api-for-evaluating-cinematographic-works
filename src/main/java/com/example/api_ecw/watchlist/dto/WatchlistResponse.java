package com.example.api_ecw.watchlist.dto;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;

import java.time.LocalDateTime;
import java.util.UUID;

public record WatchlistResponse(
        UUID id,
        UUID workId,
        UUID userId,
        String name,
        WorkType type,
        WorkStatus status,
        LocalDateTime createdAt
) {
}
