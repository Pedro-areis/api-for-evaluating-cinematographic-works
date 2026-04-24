package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.dto.TmdbGenre;
import com.example.api_ecw.watchlist.dto.WatchlistResponse;
import com.example.api_ecw.works.TmdbIntegrationService;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import com.example.api_ecw.works.dto.TmdbMovieResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WatchlistService {
    private final WatchlistRepository watchlistRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final TmdbIntegrationService tmdbIntegrationService;

    @Transactional
    public WatchlistResponse addMovieToWatchlist(UUID userId, Integer tmdbId) {
        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> createWorkFromTmdbId(tmdbId));

        User user = userRepository.findById(userId)
                .orElseThrow();

        boolean existingWatchlist = watchlistRepository.existsByUserAndWork(user, work);
        if (existingWatchlist) {
            throw new DataIntegrityViolationException("Work already in watchlist");
        }

        Watchlist watchlist = new Watchlist();

        watchlist.setName(work.getTitle());
        watchlist.setUser(user);
        watchlist.setWork(work);
        watchlist.setType(WorkType.movie);
        watchlist.setStatus(WorkStatus.pending);

        Watchlist savedWatchlist = watchlistRepository.save(watchlist);

        return new WatchlistResponse(
                savedWatchlist.getId(),
                savedWatchlist.getWork().getId(),
                savedWatchlist.getUser().getId(),
                savedWatchlist.getName(),
                savedWatchlist.getType(),
                savedWatchlist.getStatus(),
                savedWatchlist.getCreatedAt()
        );

    }

    private Work createWorkFromTmdbId(Integer tmdbId) {
        TmdbMovieResponse response = tmdbIntegrationService.getMovieByTmdbId(tmdbId);

        Work newWork = new Work();

        newWork.setTmdbId(response.id());
        newWork.setTitle(response.title());
        newWork.setSynopsis(response.overview());
        newWork.setType(WorkType.movie);
        newWork.setReleaseDate(LocalDate.parse(response.releaseDate()));

        List<Integer> genreIds = response.genres()
                        .stream()
                                .map(TmdbGenre::id)
                                .toList();

        newWork.setGenreIds(genreIds);

        workRepository.save(newWork);

        return newWork;
    }
}
