package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.tmdb_api.dto.TmdbTvResponse;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.tmdb_api.dto.TmdbGenre;
import com.example.api_ecw.watchlist.dto.RemoveWorkFromWatchlist;
import com.example.api_ecw.watchlist.dto.WatchlistResponse;
import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.watchlist.dto.WatchlistUpdated;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import com.example.api_ecw.tmdb_api.dto.TmdbMovieResponse;
import jakarta.persistence.EntityNotFoundException;
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
                .orElseGet(() -> createMovieFromTmdbId(tmdbId));

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

    @Transactional
    public WatchlistResponse addTvToWatchlist (UUID userId, Integer tmdbId) {
        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> createTvFromTmdbId(tmdbId));

        User user = userRepository.findById(userId)
                .orElseThrow();

        boolean existingWatchlist = watchlistRepository.existsByUserAndWork(user, work);
        if (existingWatchlist) {
            throw new DataIntegrityViolationException("Work already in watchlist");
        }

        Watchlist watchlist = new Watchlist();

        watchlist.setName(work.getTitle());
        watchlist.setName(work.getTitle());
        watchlist.setUser(user);
        watchlist.setWork(work);
        watchlist.setType(WorkType.series);
        watchlist.setStatus(WorkStatus.pending);

        Watchlist savedWatchlist = watchlistRepository.save(watchlist);

        return new  WatchlistResponse(
                savedWatchlist.getId(),
                savedWatchlist.getWork().getId(),
                savedWatchlist.getUser().getId(),
                savedWatchlist.getName(),
                savedWatchlist.getType(),
                savedWatchlist.getStatus(),
                savedWatchlist.getCreatedAt()
        );
    }

    private Work createTvFromTmdbId(Integer tmdbId) {
        TmdbTvResponse response = tmdbIntegrationService.getTvByTmdbId(tmdbId);

        Work newWork = new Work();

        newWork.setTmdbId(response.id());
        newWork.setTitle(response.name());
        newWork.setSynopsis(response.overview());
        newWork.setType(WorkType.series);
        newWork.setReleaseDate(LocalDate.parse(response.releaseDate()));

        List<Integer> genreIds = response.genres()
                .stream()
                .map(TmdbGenre::id)
                .toList();

        newWork.setGenreIds(genreIds);

        workRepository.save(newWork);

        return newWork;
    }

    private Work createMovieFromTmdbId(Integer tmdbId) {
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

    public List<WatchlistResponse> getAllWorksFromWatchlist(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        List<Watchlist> works = watchlistRepository.findAllByUserId(user.getId());

        return works.stream()
                .map(this::convertFromDTO)
                .toList();
    }

    private WatchlistResponse convertFromDTO(Watchlist watchlist) {
        return new  WatchlistResponse(
                watchlist.getId(),
                watchlist.getWork().getId(),
                watchlist.getUser().getId(),
                watchlist.getName(),
                watchlist.getType(),
                watchlist.getStatus(),
                watchlist.getCreatedAt()
        );
    }

    public WatchlistUpdated updateStatusForWatched(UUID userId, UUID workId) {

        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Watchlist watchlist = watchlistRepository.findByUserIdAndWorkId(user.getId(), work.getId())
                .orElseThrow(() -> new EntityNotFoundException("Watchlist not found"));

        if (watchlist.getStatus() == WorkStatus.watched) {
            throw new DataIntegrityViolationException("Work already watched");
        }
        watchlist.setStatus(WorkStatus.watched);

        watchlistRepository.save(watchlist);

        return new WatchlistUpdated(
                watchlist.getWork().getId(),
                watchlist.getName(),
                watchlist.getType(),
                watchlist.getStatus()
        );
    }

    public RemoveWorkFromWatchlist removeWork(UUID userId, UUID workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Watchlist watchlist = watchlistRepository.findByUserIdAndWorkId(user.getId(), work.getId())
                .orElseThrow(() -> new EntityNotFoundException("Watchlist not found"));

        RemoveWorkFromWatchlist response = new RemoveWorkFromWatchlist(
                watchlist.getWork().getId(),
                watchlist.getName(),
                watchlist.getType(),
                "Work removed from watchlist"
        );

        watchlistRepository.delete(watchlist);

        return response;
    }

}
