package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.scores.ScoreRepository;
import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.tmdb_api.dto.TmdbGenre;
import com.example.api_ecw.tmdb_api.dto.TmdbMovieResponse;
import com.example.api_ecw.tmdb_api.dto.TmdbTvResponse;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.dto.AllWatchlistResponse;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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

    private Work movie;
    private Work tv;

    @BeforeEach
    void setUp() {
        movie = new Work(
                UUID.randomUUID(), "movie","synopsis", 10.0f,
                WorkType.movie, LocalDate.parse("2023-01-01"), LocalDateTime.now(),
                List.of(1,2,3), 1
        );

        tv = new Work(
                UUID.randomUUID(), "series", "synopsis", 10.0f, WorkType.series,
                LocalDate.parse("2023-01-01"), LocalDateTime.now(),
                List.of(1,2,3), 1
        );
    }

    @Nested
    class addMovieToWatchlist {
        @Test
        @DisplayName("Should add movie to watchlist with success")
        void shouldAddMovieToWatchlist() {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.movie)).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(user, movie, WorkType.movie))
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
            assertEquals(movie, saved.getWork());
            assertEquals(user, saved.getUser());

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should add movie to database when not exist")
        void shouldAddMovieToDataBaseWhenNotExist() {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            TmdbMovieResponse response = new TmdbMovieResponse(
                    1,
                    "originalTitle",
                    "title",
                    "pt-BR",
                    "1999-01-01",
                    "overview",
                    List.of(
                            new TmdbGenre(1, "test")
                    )
            );

            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.movie)).thenReturn(Optional.empty());
            when(tmdbIntegrationService.getMovieByTmdbId(tmdbId)).thenReturn(response);

            when(workRepository.save(any(Work.class))).thenAnswer(i -> i.getArgument(0));

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(
                    eq(user),
                    any(Work.class),
                    eq(WorkType.movie)
            )).thenReturn(false);

            when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            var result = watchlistService.addMovieToWatchlist(userId, tmdbId);

            // Assert
            verify(watchlistRepository).save(watchlistCaptor.capture());

            Watchlist saved = watchlistCaptor.getValue();

            assertEquals("title", saved.getName());
            assertEquals(WorkStatus.pending, saved.getStatus());
            assertEquals(WorkType.movie, saved.getType());
            assertEquals(user, saved.getUser());

            assertNotNull(saved.getWork());
            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldExceptionWhenUserNotFound() {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.movie)).thenReturn(Optional.of(movie));
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.addMovieToWatchlist(userId, tmdbId));

            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when work exists by user and same type")
        void shouldExceptionWhenWorkExistsByUserAndSameType() {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.movie)).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(user, movie, WorkType.movie))
                    .thenReturn(true);

            // Act & Assert
            assertThrows(DataIntegrityViolationException.class,
                    () -> watchlistService.addMovieToWatchlist(userId, tmdbId));

            verify(watchlistRepository, never()).save(any());
        }
    }

    @Nested
    class addTvToWatchlist {
        @Test
        @DisplayName("Should add tv to watchlist with success")
        void shouldAddTvToWatchlist() {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.movie)).thenReturn(Optional.of(tv));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(user, tv, WorkType.movie))
                    .thenReturn(false);
            when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            var result = watchlistService.addMovieToWatchlist(userId, tmdbId);

            // Assert
            verify(watchlistRepository).save(watchlistCaptor.capture());

            Watchlist saved = watchlistCaptor.getValue();

            assertEquals("series", saved.getName());
            assertEquals(WorkStatus.pending, saved.getStatus());
            assertEquals(WorkType.movie, saved.getType());
            assertEquals(tv, saved.getWork());
            assertEquals(user, saved.getUser());

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should add tv to database when not exist")
        void shouldAddTvToDataBaseWhenNotExist () {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            TmdbTvResponse response = new TmdbTvResponse(
                    1,
                    "originalName",
                    "name",
                    "pt-BR",
                    "1999-01-01",
                    "overview",
                    List.of(
                            new TmdbGenre(1, "test")
                    )
            );

            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.series)).thenReturn(Optional.empty());
            when(tmdbIntegrationService.getTvByTmdbId(tmdbId)).thenReturn(response);

            when(workRepository.save(any(Work.class))).thenAnswer(i -> i.getArgument(0));

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(
                    eq(user),
                    any(Work.class),
                    eq(WorkType.series)
            )).thenReturn(false);

            when(watchlistRepository.save(any(Watchlist.class))).thenAnswer(i -> i.getArgument(0));

            // Act
            var result = watchlistService.addTvToWatchlist(userId, tmdbId);

            // Assert
            verify(watchlistRepository).save(watchlistCaptor.capture());

            Watchlist saved = watchlistCaptor.getValue();

            assertEquals("name", saved.getName());
            assertEquals(WorkStatus.pending, saved.getStatus());
            assertEquals(WorkType.series, saved.getType());
            assertEquals(user, saved.getUser());

            assertNotNull(saved.getWork());
            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldExceptionWhenUserNotFound () {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.series)).thenReturn(Optional.of(tv));
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.addTvToWatchlist(userId, tmdbId));

            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when tv exists by user and same type")
        void shouldExceptionWhenTvExistsByUserAndSameType () {
            // Arrange
            Integer tmdbId = 1;
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            when(workRepository.findByTmdbIdAndType(tmdbId, WorkType.series)).thenReturn(Optional.of(tv));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.existsByUserAndWorkAndType(user, tv, WorkType.series))
                    .thenReturn(true);

            // Act & Assert
            assertThrows(DataIntegrityViolationException.class,
                    () -> watchlistService.addTvToWatchlist(userId, tmdbId));

            verify(watchlistRepository, never()).save(any());
        }
    }

    @Nested
    class createMovieFromTmdbId {
        @Test
        @DisplayName("Should map tmdb response to work correctly")
        void shouldMapTmdbResponseToWorkCorrectly () {
            // Arrange
            Integer tmdbId = 123;
            TmdbMovieResponse response = new TmdbMovieResponse(tmdbId, "originalTitle",
                    "title", "pt-BR", "1999-01-01", "overview", List.of(
                            new TmdbGenre(1, "test")
                    ));
            when(tmdbIntegrationService.getMovieByTmdbId(tmdbId))
                    .thenReturn(response);
            when(workRepository.save(any(Work.class))).thenAnswer(i -> i.getArgument(0));
            // Act
            Work result = watchlistService.createMovieFromTmdbId(tmdbId);

            // Assert
            assertEquals("title", result.getTitle());
            assertEquals(LocalDate.of(1999, 1, 1), result.getReleaseDate());
            assertEquals(List.of(1), result.getGenreIds());

            verify(workRepository).save(any(Work.class));
        }
    }

    @Nested
    class createTvFromTmdbId {
        @Test
        @DisplayName("Should map tmdb response to work correctly")
        void shouldMapTmdbResponseToWorkCorrectly () {
            // Arrange
            Integer tmdbId = 123;
            TmdbTvResponse response = new TmdbTvResponse(tmdbId, "originalName",
                    "name", "pt-BR", "1999-01-01", "overview", List.of(
                    new TmdbGenre(1, "test")
            ));
            when(tmdbIntegrationService.getTvByTmdbId(tmdbId))
                    .thenReturn(response);
            when(workRepository.save(any(Work.class))).thenAnswer(i -> i.getArgument(0));
            // Act
            Work result = watchlistService.createTvFromTmdbId(tmdbId);

            // Assert
            assertEquals("name", result.getTitle());
            assertEquals(LocalDate.of(1999, 1, 1), result.getReleaseDate());
            assertEquals(List.of(1), result.getGenreIds());

            verify(workRepository).save(any(Work.class));
        }
    }

    @Nested
    class getAllWorksFromWatchlist {
        @Test
        @DisplayName("Should get all works from watchlist with success")
        void shouldGetAllWorksFromWatchlistWithSuccess () {
            // Arrange
            UUID userId = UUID.randomUUID();
            UUID watchlistId = UUID.randomUUID();

            User user = new User();
            user.setId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            Watchlist watchlist = new Watchlist(watchlistId, "workName", user,
                    movie, WorkType.movie, WorkStatus.pending, LocalDateTime.now());

            when(watchlistRepository.findAllByUserId(user.getId()))
                    .thenReturn(List.of(watchlist));
            // Act
            List<AllWatchlistResponse> result = watchlistService.getAllWorksFromWatchlist(userId);

            // Assert
            assertEquals(1, result.size());
            assertEquals("workName", result.getFirst().name());
            assertEquals(WorkType.movie, result.getFirst().type());
            assertEquals(WorkStatus.pending, result.getFirst().status());

            verify(watchlistRepository).findAllByUserId(userId);
        }

        @Test
        @DisplayName("Should exception when User not found")
        void shouldExceptionWhenUserNotFound () {
            // Arrange
            UUID userId = UUID.randomUUID();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.getAllWorksFromWatchlist(userId));

            verify(watchlistRepository, never()).save(any());
        }
    }

    @Nested
    class updateStatusForWatched {
        @Test
        @DisplayName("Should to update work for watched and add score")
        void shouldToUpdateWorkForWatchedAndAddScore () {
            // Arrange
            BigDecimal score = BigDecimal.TEN;

            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            Watchlist watchlist = new Watchlist(UUID.randomUUID(), "workName", user,
                    movie, WorkType.movie, WorkStatus.pending, LocalDateTime.now());

            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.findByUserIdAndWorkId(userId, movie.getId()))
                    .thenReturn(Optional.of(watchlist));

            // Act
            var result = watchlistService.updateStatusForWatched(score, userId, movie.getId());

            // Assert
            verify(watchlistRepository).save(watchlistCaptor.capture());

            Watchlist saved = watchlistCaptor.getValue();

            assertEquals("workName", saved.getName());
            assertEquals(WorkType.movie, saved.getType());
            assertEquals(WorkStatus.watched, saved.getStatus());
            assertEquals(10.0f, saved.getWork().getScore());

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should exception when Work not found")
        void shouldExceptionWhenWorkNotFound () {
            // Arrange
            UUID userId = UUID.randomUUID();
            when(workRepository.findById(movie.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.updateStatusForWatched(BigDecimal.TEN, userId, movie.getId()));

            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should exception when User not found")
        void shouldExceptionWhenUserNotFound () {
            // Arrange
            UUID userId = UUID.randomUUID();
            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.updateStatusForWatched(BigDecimal.TEN, userId, movie.getId()));

            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should exception when Watchlist not found")
        void shouldExceptionWhenWatchlistNotFound () {
            // Arrange
            UUID userId = UUID.randomUUID();
            User user = new  User();
            user.setId(userId);

            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.findByUserIdAndWorkId(userId, movie.getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(BadCredentialsException.class,
                    () -> watchlistService.updateStatusForWatched(BigDecimal.TEN, userId, movie.getId()));
            verify(watchlistRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should exception when work in watchlist already gone watch")
        void shouldExceptionWhenWorkInWatchlistAlreadyGoneWatched () {
            // Arrange
            UUID userId = UUID.randomUUID();
            User user = new  User();
            user.setId(userId);

            Watchlist watchlist = new Watchlist(UUID.randomUUID(), "workName", user,
                    movie, WorkType.movie, WorkStatus.watched, LocalDateTime.now());

            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.findByUserIdAndWorkId(userId, movie.getId()))
                    .thenReturn(Optional.of(watchlist));

            // Act & Assert
            assertThrows(DataIntegrityViolationException.class,
                    () -> watchlistService.updateStatusForWatched(BigDecimal.TEN, userId, movie.getId()));
            verify(watchlistRepository, never()).save(any());
        }
    }

    @Nested
    class removeWork {
        @Test
        @DisplayName("Should delete Work with success")
        void shouldDeleteWorkWithSuccess () {
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            Watchlist watchlist = new Watchlist(UUID.randomUUID(), "workName", user,
                    movie, WorkType.movie, WorkStatus.pending, LocalDateTime.now());

            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.findByUserIdAndWorkId(userId, movie.getId()))
                    .thenReturn(Optional.of(watchlist));

            var result = watchlistService.removeWork(userId, movie.getId());

            verify(watchlistRepository).delete(watchlistCaptor.capture());

            Watchlist saved = watchlistCaptor.getValue();

            assertEquals("workName", saved.getName());
            assertEquals(WorkType.movie, saved.getType());
            assertEquals(WorkStatus.pending, saved.getStatus());

            assertNotNull(result);
        }

        @Test
        @DisplayName("Should Exception When Work Not Found")
        void shouldExceptionWhenWorkNotFound () {
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            when(workRepository.findById(movie.getId())).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.removeWork(userId, movie.getId()));
            verify(watchlistRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should Exception when User Not Found")
        void shouldExceptionWhenUserNotFound () {
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class,
                    () -> watchlistService.removeWork(userId, movie.getId()));
            verify(watchlistRepository, never()).delete(any());
        }

        @Test
        @DisplayName("Should exception when watchlist not found")
        void shouldExceptionWhenWatchlistNotFound () {
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            when(workRepository.findById(movie.getId())).thenReturn(Optional.of(movie));
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(watchlistRepository.findByUserIdAndWorkId(userId, movie.getId()))
                    .thenReturn(Optional.empty());

            assertThrows(BadCredentialsException.class,
                    () -> watchlistService.removeWork(userId, movie.getId()));
            verify(watchlistRepository, never()).delete(any());
        }
    }
}