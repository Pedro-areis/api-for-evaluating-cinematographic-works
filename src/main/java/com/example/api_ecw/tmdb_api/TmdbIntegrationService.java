package com.example.api_ecw.tmdb_api;

import com.example.api_ecw.tmdb_api.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
public class TmdbIntegrationService {
    private final RestClient restClient;
    private final String apiKey;

    public TmdbIntegrationService (
            @Value("${tmdb.api.url}") String apiUrl,
            @Value("${tmdb.api.key}") String apiKey) {

        this.apiKey = apiKey;
        restClient = RestClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("accept", "application/json")
                .build();
    }

    public TmdbSearchResponse getWorkByTitle(String title) {
        return restClient.get()
                .uri("/search/multi?query={title}&include_adult=false&language=pt-BR&page=1&api_key={apiKey}", title, apiKey)
                .retrieve()
                .body(TmdbSearchResponse.class);
    }

    public TmdbMovieResponse getMovieByTmdbId(Integer tmdbId) {
        return restClient.get()
                .uri("/movie/{tmdbId}?language=pt-BR&api_key={apiKey}", tmdbId, apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new EntityNotFoundException("Movie not found in TMDB API, with tmdbId: " + tmdbId);
                })
                .body(TmdbMovieResponse.class);
    }

    public TmdbTvResponse getTvByTmdbId(Integer tmdbId) {
        return restClient.get()
                .uri("/tv/{tmdbId}?language=pt-BR&api_key={apiKey}", tmdbId, apiKey)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                    throw new EntityNotFoundException("TV not found in TMDB API, with tmdbId: " + tmdbId);
                })
                .body(TmdbTvResponse.class);
    }

    public TmdbResponseGenre getAllMoviesGenre() {
        return restClient.get()
                .uri("/genre/movie/list?language=pt-BR&api_key={apiKey}", apiKey)
                .retrieve()
                .body(TmdbResponseGenre.class);
    }

    public TmdbResponseGenre getAllTvGenre() {
        return restClient.get()
                .uri("/genre/tv/list?language=pt-BR&api_key={apiKey}", apiKey)
                .retrieve()
                .body(TmdbResponseGenre.class);
    }
}
