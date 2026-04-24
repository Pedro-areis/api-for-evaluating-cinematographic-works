package com.example.api_ecw.tmdb_api.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbWorkResponse(
        Integer id,
        @JsonProperty("original_title") @JsonAlias("original_name") String originalTitle,
        @JsonProperty("title") @JsonAlias("name") String title,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("release_date") @JsonAlias("first_air_date") String releaseDate,
        String overview,
        @JsonProperty("media_type") String mediaType,
        @JsonProperty("genre_ids") List<Integer> genreIds
) {}
