package com.example.api_ecw.posts;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.posts.dto.PostRequest;
import com.example.api_ecw.posts.dto.PostResponse;
import com.example.api_ecw.tmdb_api.GenreCacheService;
import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.tmdb_api.dto.TmdbGenre;
import com.example.api_ecw.tmdb_api.dto.TmdbMovieResponse;
import com.example.api_ecw.tmdb_api.dto.TmdbTvResponse;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.WatchlistService;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final WatchlistService watchlistService;
    private final GenreCacheService genreCacheService;

    @Transactional
    public PostResponse createPostFromMovie(PostRequest request, UUID userId, Integer tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> watchlistService.createMovieFromTmdbId(tmdbId));

        Post post = new Post();

        post.setUser(user);
        post.setWork(work);
        post.setContent(request.content());

        Post savedPost = postRepository.save(post);

        List<String> genres = work.getGenreIds().stream()
                .map(genreCacheService::getMovieGenreNameById)
                .collect(Collectors.toList());

        return new PostResponse(
                savedPost.getId(),
                user.getName(),
                work.getTitle(),
                work.getSynopsis(),
                genres,
                work.getType(),
                work.getReleaseDate(),
                savedPost.getContent(),
                LocalDateTime.now()
        );
    }

    @Transactional
    public PostResponse createPostFromTv(PostRequest request, UUID userId, Integer tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> watchlistService.createTvFromTmdbId(tmdbId));

        Post post = new Post();

        post.setUser(user);
        post.setWork(work);
        post.setContent(request.content());

        Post savedPost = postRepository.save(post);

        List<String> genres = work.getGenreIds().stream()
                .map(genreCacheService::getTvGenreNameById)
                .collect(Collectors.toList());

        return new PostResponse(
                savedPost.getId(),
                user.getName(),
                work.getTitle(),
                work.getSynopsis(),
                genres,
                work.getType(),
                work.getReleaseDate(),
                savedPost.getContent(),
                LocalDateTime.now()
        );
    }

    public List<PostResponse> getAllPostsFromWork (UUID workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        List<Post> posts = postRepository.findAllByWorkId(work.getId());

        return posts.stream()
                .map(this::convertPostToPostResponse)
                .collect(Collectors.toList());
    }

    private PostResponse convertPostToPostResponse(Post post) {
        List<String> genres = post.getWork().getGenreIds().stream()
                .map(genreCacheService::getMovieGenreNameById)
                .collect(Collectors.toList());

        return new PostResponse(
                post.getId(),
                post.getUser().getName(),
                post.getWork().getTitle(),
                post.getWork().getSynopsis(),
                genres,
                post.getWork().getType(),
                post.getWork().getReleaseDate(),
                post.getContent(),
                post.getPostDate()
        );
    }
}
