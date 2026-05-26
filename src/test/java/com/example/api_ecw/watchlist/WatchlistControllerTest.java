package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.infra.config.TokenService;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.dto.AllWatchlistResponse;
import com.example.api_ecw.watchlist.dto.WatchlistResponse;
import com.example.api_ecw.works.WorkRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WatchlistController.class)
class WatchlistControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private WatchlistService watchlistService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private WorkRepository workRepository;

    private WatchlistResponse responseMovie;
    private WatchlistResponse responseTv;
    private User loggedUser;
    private UsernamePasswordAuthenticationToken authToken;

    @BeforeEach
    void setUp() {
        loggedUser = new User();
        loggedUser.setId(UUID.randomUUID());

        responseMovie = new WatchlistResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                loggedUser.getId(),
                "name",
                WorkType.movie,
                WorkStatus.pending,
                LocalDateTime.now()
        );

        responseTv = new WatchlistResponse(
                UUID.randomUUID(),
                UUID.randomUUID(),
                loggedUser.getId(),
                "name",
                WorkType.series,
                WorkStatus.pending,
                LocalDateTime.now()
        );

        authToken = new UsernamePasswordAuthenticationToken (loggedUser,null, List.of());
    }

    @Nested
    class addMovieToWatchlist {
        @Test
        @DisplayName("Should return 201 when add a movie to watchlist")
        void shouldReturn201_WhenAddMovieToWatchlist () throws Exception {
            // Arrange
            Integer tmdbId = 1;
            when(watchlistService.addMovieToWatchlist(loggedUser.getId(), tmdbId)).thenReturn(responseMovie);

            // Act & Assert
            String json = objectMapper.writeValueAsString(responseMovie);

            mockMvc.perform(post("/api/watchlist/add-movie/{tmdbId}", tmdbId)
                        .with(csrf())
                        .with(authentication(authToken)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("Should return 400 when tmdb id for movie not valid")
        void shouldReturn400_WhenTmdbIdForMovieNotValid () throws Exception {
            // Arrange
            String invalidReq = "invalid";
            // Act & Assert
            mockMvc.perform(post("/api/watchlist/add-movie/{tmdbId}", invalidReq)
                        .with(csrf())
                        .with(authentication(authToken)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 when tmdb id not exists")
        void shouldReturn404_WhenTmdbIdNotExists () throws Exception {
            //Arrange
            Integer tmdbId = 999999;
            when(watchlistService.addMovieToWatchlist(loggedUser.getId(), tmdbId))
                    .thenThrow(new EntityNotFoundException("Work not found in TMDB"));

            // Act & Assert
            mockMvc.perform(post("/api/watchlist/add-movie/{tmdbId}", tmdbId)
                        .with(csrf())
                        .with(authentication(authToken)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class addTvToWatchlist {
        @Test
        @DisplayName("Should return 201 when add a tv to watchlist")
        void shouldReturn201_WhenAddTvToWatchlist () throws Exception {
            // Arrange
            Integer tmdbId = 2;
            when(watchlistService.addTvToWatchlist(loggedUser.getId(), tmdbId)).thenReturn(responseTv);

            // Act & Assert
            String json = objectMapper.writeValueAsString(responseTv);

            mockMvc.perform(post("/api/watchlist/add-tv/{tmdbId}", tmdbId)
                            .with(csrf())
                            .with(authentication(authToken)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("Should return 400 when tmdb id for tv not valid")
        void shouldReturn400_WhenTmdbIdForTvNotValid () throws Exception {
            // Arrange
            String invalidReq = "invalid";
            // Act & Assert
            mockMvc.perform(post("/api/watchlist/add-tv/{tmdbId}", invalidReq)
                            .with(csrf())
                            .with(authentication(authToken)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 when tmdb id not exists")
        void shouldReturn404_WhenTmdbIdNotExists () throws Exception {
            //Arrange
            Integer tmdbId = 999999;
            when(watchlistService.addTvToWatchlist(loggedUser.getId(), tmdbId))
                    .thenThrow(new EntityNotFoundException("Work not found in TMDB"));

            // Act & Assert
            mockMvc.perform(post("/api/watchlist/add-tv/{tmdbId}", tmdbId)
                            .with(csrf())
                            .with(authentication(authToken)))
                    .andExpect(status().isNotFound());
        }
    }

//    @Nested
//    class getWatchlist {
//        @Test
//        @DisplayName("Should return 200 when get watchlist for user")
//        void shouldReturn200_WhenGetWatchlistForUser () throws Exception {
//            // Arrange
//            AllWatchlistResponse responseList = new AllWatchlistResponse(
//                    UUID.randomUUID(),
//                    UUID.randomUUID(),
//                    loggedUser.getId(),
//                    "name",
//                    10.0f,
//                    WorkType.movie,
//                    WorkStatus.pending,
//                    LocalDateTime.now()
//            );
//            when(watchlistService.getAllWorksFromWatchlist(loggedUser.getId()))
//                    .thenReturn(List.of(responseList));
//
//            String json = objectMapper.writeValueAsString(responseList);
//
//            // Act & Assert
//            mockMvc.perform(get("/api/watchlist/my-watchlist")
//                        .with(csrf())
//                        .with(authentication(authToken)))
//                    .andExpect(status().isOk())
//                    .andExpect(content().json(json));
//        }
//    }
}