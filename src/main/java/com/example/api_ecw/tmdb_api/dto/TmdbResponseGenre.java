package com.example.api_ecw.tmdb_api.dto;

import java.util.List;

public record TmdbResponseGenre(
        List<TmdbGenre> genres
) {
}
