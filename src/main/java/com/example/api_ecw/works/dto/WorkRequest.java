package com.example.api_ecw.works.dto;

import com.example.api_ecw.enums.WorkType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record WorkRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Synopsis is required")
        String synopsis,

        @NotNull(message = "Type is required")
        WorkType type,

        @NotNull(message = "Genre IDs is required")
        List<Integer> genreIds,

        @NotNull(message = "TMDB ID is required")
        Integer tmdbId,

        @NotNull(message = "Release date is required")
        LocalDate releaseDate
) {
}
