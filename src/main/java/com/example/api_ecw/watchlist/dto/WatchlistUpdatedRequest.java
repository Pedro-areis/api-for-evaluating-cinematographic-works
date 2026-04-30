package com.example.api_ecw.watchlist.dto;

import java.math.BigDecimal;

public record WatchlistUpdatedRequest (
        BigDecimal workScore
) {}
