package com.example.api_ecw.post_likes.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostLikesResponse(
        UUID id,
        String userName,
        LocalDateTime likeDate
) {}
