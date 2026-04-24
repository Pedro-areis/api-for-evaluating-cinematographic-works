package com.example.api_ecw.tmdb_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbMovieResponse(
        Integer id,
        @JsonProperty("original_title") String originalTitle,
        @JsonProperty("title") String title,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("release_date") String releaseDate,
        String overview,
        @JsonProperty("genres") List<TmdbGenre> genres
) {}
