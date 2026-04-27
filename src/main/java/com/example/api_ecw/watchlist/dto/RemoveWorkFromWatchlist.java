package com.example.api_ecw.watchlist.dto;

import com.example.api_ecw.enums.WorkType;

import java.util.UUID;

public record RemoveWorkFromWatchlist(
        UUID workId,
        String name,
        WorkType type,
        String message
) {
}
