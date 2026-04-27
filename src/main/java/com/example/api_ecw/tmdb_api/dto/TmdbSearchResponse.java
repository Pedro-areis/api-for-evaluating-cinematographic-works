package com.example.api_ecw.tmdb_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbSearchResponse(
        List<TmdbWorkResponse> results,
        Integer page,
        @JsonProperty("total_pages") Integer totalPages,
        @JsonProperty("total_results") Integer totalResults
) {}
