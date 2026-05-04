package com.example.api_ecw.comment_likes.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentLikesResponse(
        UUID id,
        String userName,
        LocalDateTime likeDate
) {}
