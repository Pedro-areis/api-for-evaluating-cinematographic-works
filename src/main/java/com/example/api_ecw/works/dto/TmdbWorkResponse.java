package com.example.api_ecw.works.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Array;
import java.util.List;

public record TmdbWorkResponse(
        Long id,
        @JsonProperty("original_title") String originalTitle,
        String title,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("release_date") String releaseDate,
        String overview,
        @JsonProperty("genre_ids") List<Integer> genreIds
) {}
