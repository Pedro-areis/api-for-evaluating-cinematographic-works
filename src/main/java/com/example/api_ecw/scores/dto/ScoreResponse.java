package com.example.api_ecw.scores.dto;

import java.math.BigDecimal;

public record ScoreResponse(
        String workTitle,
        String reviewerName,
        BigDecimal myScore
) {
}
