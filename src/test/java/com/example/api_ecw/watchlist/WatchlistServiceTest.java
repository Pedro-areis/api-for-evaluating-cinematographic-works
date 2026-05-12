package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.scores.ScoreRepository;
import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatchlistServiceTest {
    @Captor
    private ArgumentCaptor<Watchlist> watchlistCaptor;
    @Mock
    private WatchlistRepository watchlistRepository;
    @Mock
    private WorkRepository workRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TmdbIntegrationService tmdbIntegrationService;
    @Mock
    private ScoreRepository scoreRepository;

    @InjectMocks
    private WatchlistService watchlistService;

    @Nested
    class addMovieToWatchlist {
        @Test
        @DisplayName("Should add movie to watchlist with success")
        void shouldAddMovieToWatchlist() {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            Work work = new Work(
                    UUID.randomUUID(),
                    "movie",
                    "synopsis",
                    10.0f,
                    WorkType.movie,
                    LocalDate.parse("2023-01-01"),
                    LocalDateTime.now(),
                    List.of(1,2,3),
                    tmdbId
            );

            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbId(tmdbId)).thenReturn(Optional.of(work));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(user, work, WorkType.movie))
                    .thenReturn(false);
            when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            var result = watchlistService.addMovieToWatchlist(userId, tmdbId);

            // Assert
            verify(watchlistRepository).save(watchlistCaptor.capture());

            Watchlist saved = watchlistCaptor.getValue();

            assertEquals("movie", saved.getName());
            assertEquals(WorkStatus.pending, saved.getStatus());
            assertEquals(WorkType.movie, saved.getType());
            assertEquals(work, saved.getWork());
            assertEquals(user, saved.getUser());

            assertNotNull(result);
        }
    }
}