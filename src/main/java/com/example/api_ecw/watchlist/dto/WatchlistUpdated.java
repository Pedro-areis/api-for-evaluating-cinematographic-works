package com.example.api_ecw.watchlist.dto;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;

import java.util.UUID;

public record WatchlistUpdated(
        UUID workId,
        String name,
        WorkType type,
        WorkStatus status
) {
}
