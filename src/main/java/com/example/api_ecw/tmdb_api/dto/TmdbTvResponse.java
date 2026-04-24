package com.example.api_ecw.tmdb_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbTvResponse(
        Integer id,
        @JsonProperty("original_name") String originalName,
        @JsonProperty("name") String name,
        @JsonProperty("original_language") String originalLanguage,
        @JsonProperty("first_air_date") String releaseDate,
        String overview,
        @JsonProperty("genres") List<TmdbGenre> genres
) {
}
