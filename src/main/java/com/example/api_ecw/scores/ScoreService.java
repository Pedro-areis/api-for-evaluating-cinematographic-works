package com.example.api_ecw.scores;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.scores.dto.EditScoreRequest;
import com.example.api_ecw.scores.dto.ScoreRequest;
import com.example.api_ecw.scores.dto.ScoreResponse;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.WatchlistService;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScoreService {
    private final ScoreRepository scoreRepository;
    private final WorkRepository workRepository;
    private final UserRepository userRepository;
    private final WatchlistService watchlistService;

    @Transactional
    public ScoreResponse giveScore(ScoreRequest request, UUID userId, Integer tmdbId) {
        Work work;
        if (request.type() == WorkType.movie) {
            work = workRepository.findByTmdbIdAndType(tmdbId, request.type())
                    .orElseGet(() -> watchlistService.createMovieFromTmdbId(tmdbId));
        } else {
            work = workRepository.findByTmdbIdAndType(tmdbId, request.type())
                    .orElseGet(() -> watchlistService.createTvFromTmdbId(tmdbId));
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (scoreRepository.existsByUserAndWork(user, work)) {
            throw new DataIntegrityViolationException("User and Work already have a score");
        }

        Score score = new Score();
        score.setUser(user);
        score.setWork(work);
        score.setScore(request.score());

        Score savedScore = scoreRepository.save(score);

        return new ScoreResponse(
                work.getTitle(),
                user.getName(),
                savedScore.getScore()
        );
    }

    public ScoreResponse editScore(EditScoreRequest request, UUID workId, UUID userId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Score score = scoreRepository.findByUserAndWork(user, work)
                .orElseThrow(() -> new BadCredentialsException("Score not found or not owned by user"));

        score.setScore(request.score());
        scoreRepository.save(score);

        return new ScoreResponse(
                work.getTitle(),
                user.getName(),
                score.getScore()
        );
    }
}
