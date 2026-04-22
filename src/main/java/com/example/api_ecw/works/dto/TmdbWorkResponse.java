package com.example.api_ecw.works.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Array;
import java.util.List;

public record TmdbWorkResponse(
        Long id,
        @JsonProperty("original_title") @JsonAlias("original_name") String originalTitle,
        @JsonProperty("title") @JsonAlias("name") String title,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("release_date") @JsonAlias("first_air_date") String releaseDate,
        String overview,
        @JsonProperty("media_type") String mediaType,
        @JsonProperty("genre_ids") List<Integer> genreIds
) {}
