package com.example.api_ecw.comments.dto;

import java.util.List;
import java.util.UUID;

public record AllCommentsFromPost (
        UUID id,
        String authorName,
        String workTitle,
        String content,
        List<RepliesDTO> replies
) {}
