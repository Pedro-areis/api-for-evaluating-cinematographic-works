package com.example.api_ecw.comments.dto;

import java.util.List;
import java.util.UUID;

public record AllCommentsResponse(
        UUID mainCommentId,
        String authorName,
        String workName,
        Integer likes,
        String content,
        List<RepliesDTO> replies
) {
}
