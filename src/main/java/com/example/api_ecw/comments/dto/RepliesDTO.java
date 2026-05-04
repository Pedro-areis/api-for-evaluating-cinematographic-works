package com.example.api_ecw.comments.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RepliesDTO(
        UUID id,
        String authorName,
        Integer likes,
        String reply
) {}
