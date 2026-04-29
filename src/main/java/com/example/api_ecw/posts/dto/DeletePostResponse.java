package com.example.api_ecw.posts.dto;

import java.util.UUID;

public record DeletePostResponse (
        UUID postId,
        String authorName,
        String workTitle,
        String content,
        String message
){}
