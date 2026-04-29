package com.example.api_ecw.comments.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse (
        UUID id,
        String authorName,
        String workName,
        String contentPost,
        LocalDateTime commentDate
) {}
