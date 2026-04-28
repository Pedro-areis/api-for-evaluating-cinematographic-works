package com.example.api_ecw.posts;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.posts.dto.PostRequest;
import com.example.api_ecw.posts.dto.PostResponse;
import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.tmdb_api.dto.TmdbGenre;
import com.example.api_ecw.tmdb_api.dto.TmdbMovieResponse;
import com.example.api_ecw.tmdb_api.dto.TmdbTvResponse;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.WatchlistService;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final WatchlistService watchlistService;

    @Transactional
    public PostResponse createPostFromMovie(PostRequest request, UUID userId, Integer tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> watchlistService.createMovieFromTmdbId(tmdbId));

        Post post = new Post();

        post.setUser(user);
        post.setWork(work);
        post.setContent(request.content());

        Post savedPost = postRepository.save(post);

        return new PostResponse(
                savedPost.getId(),
                user.getName(),
                work.getTitle(),
                work.getSynopsis(),
                work.getGenreIds(),
                work.getType(),
                work.getReleaseDate(),
                savedPost.getContent(),
                LocalDateTime.now()
        );
    }


    @Transactional
    public PostResponse createPostFromTv(PostRequest request, UUID userId, Integer tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> watchlistService.createTvFromTmdbId(tmdbId));

        Post post = new Post();

        post.setUser(user);
        post.setWork(work);
        post.setContent(request.content());

        Post savedPost = postRepository.save(post);

        return new PostResponse(
                savedPost.getId(),
                user.getName(),
                work.getTitle(),
                work.getSynopsis(),
                work.getGenreIds(),
                work.getType(),
                work.getReleaseDate(),
                savedPost.getContent(),
                LocalDateTime.now()
        );
    }

}
