package com.example.api_ecw.posts.dto;

import com.example.api_ecw.enums.WorkType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AllPostsResponse (
        UUID id,
        String authorName,
        String workTitle,
        Integer likes,
        String overview,
        Float avgScore,
        List<String> genres,
        WorkType workType,
        LocalDate releaseDate,
        String content,
        LocalDateTime postDate
) {}
