package com.example.api_ecw.watchlist.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record WatchlistUpdatedRequest (
        @Schema(description = "Nota da obra", example = "7.5")
        BigDecimal workScore
) {}
