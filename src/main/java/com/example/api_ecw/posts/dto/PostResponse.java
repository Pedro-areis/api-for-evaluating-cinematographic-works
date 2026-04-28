package com.example.api_ecw.posts.dto;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.tmdb_api.dto.TmdbGenre;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String authorName,
        String workTitle,
        String overview,
        List<Integer> genres,
        WorkType workType,
        LocalDate releaseDate,
        String content,
        LocalDateTime postDate
) {
}
