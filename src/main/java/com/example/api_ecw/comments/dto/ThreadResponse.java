package com.example.api_ecw.comments.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record ThreadResponse(
        UUID mainCommentId,
        String workName,
        String authorName,
        String content,
        List<RepliesDTO> replies
) {}
