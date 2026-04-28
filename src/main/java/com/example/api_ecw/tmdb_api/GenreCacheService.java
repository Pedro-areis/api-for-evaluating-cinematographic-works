package com.example.api_ecw.tmdb_api;

import com.example.api_ecw.tmdb_api.dto.TmdbGenre;
import com.example.api_ecw.tmdb_api.dto.TmdbResponseGenre;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class GenreCacheService {
    private final TmdbIntegrationService tmdbService;
    private final HashMap<Integer, String> genresMovieMap = new HashMap<>();
    private final HashMap<Integer, String> genresTvMap = new HashMap<>();

    @PostConstruct
    public void init() {
        TmdbResponseGenre movieResponse = tmdbService.getAllMoviesGenre();
        TmdbResponseGenre tvResponse = tmdbService.getAllTvGenre();

        if (movieResponse != null && movieResponse.genres() != null) {
            for (TmdbGenre genre : movieResponse.genres()) {
                genresMovieMap.put(genre.id(), genre.name());
            }
        }

        if (tvResponse != null && tvResponse.genres() != null) {
            for (TmdbGenre genre : tvResponse.genres()) {
                genresTvMap.put(genre.id(), genre.name());
            }
        }
    }

    public String getMovieGenreNameById (Integer id) {
        return genresMovieMap.getOrDefault(id, "Unknown");
    }

    public String getTvGenreNameById (Integer id) {
        return genresTvMap.getOrDefault(id, "Unknown");
    }
}
