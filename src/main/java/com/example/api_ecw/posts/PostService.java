package com.example.api_ecw.posts;

import com.example.api_ecw.post_likes.PostLikes;
import com.example.api_ecw.post_likes.PostLikesRepository;
import com.example.api_ecw.posts.dto.*;
import com.example.api_ecw.scores.Score;
import com.example.api_ecw.scores.ScoreRepository;
import com.example.api_ecw.tmdb_api.GenreCacheService;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import com.example.api_ecw.watchlist.WatchlistService;
import com.example.api_ecw.works.Work;
import com.example.api_ecw.works.WorkRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Delete;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final ScoreRepository scoreRepository;
    private final PostLikesRepository postLikesRepository;

    @Transactional
    public PostResponse createPostFromMovie(PostRequest request, UUID userId, Integer tmdbId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Work work = workRepository.findByTmdbId(tmdbId)
                .orElseGet(() -> watchlistService.createMovieFromTmdbId(tmdbId));

        if (postRepository.existsByUserAndWork(user, work)) {
            throw new DataIntegrityViolationException("Post already exists of work");
        }

        Score score = new Score();
        score.setWork(work);
        score.setUser(user);
        score.setScore(request.score());

        scoreRepository.save(score);

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
                work.getScore(),
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

        if (postRepository.existsByUserAndWork(user, work)) {
            throw new DataIntegrityViolationException("Post already exists of work");
        }

        Score score = new Score();
        score.setWork(work);
        score.setUser(user);
        score.setScore(request.score());

        scoreRepository.save(score);

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
                work.getScore(),
                genres,
                work.getType(),
                work.getReleaseDate(),
                savedPost.getContent(),
                LocalDateTime.now()
        );
    }

    public List<AllPostsResponse> getAllPostsFromWork (UUID workId) {
        Work work = workRepository.findById(workId)
                .orElseThrow(() -> new EntityNotFoundException("Work not found"));
        List<Post> posts = postRepository.findAllByWorkId(work.getId());

        return posts.stream()
                .map(this::convertPostToPostResponse)
                .collect(Collectors.toList());
    }

    private AllPostsResponse convertPostToPostResponse(Post post) {
        List<String> genres = post.getWork().getGenreIds().stream()
                .map(genreCacheService::getMovieGenreNameById)
                .collect(Collectors.toList());

        return new AllPostsResponse (
                post.getId(),
                post.getUser().getName(),
                post.getWork().getTitle(),
                getLikesFromPost(post.getId()),
                post.getWork().getSynopsis(),
                post.getWork().getScore(),
                genres,
                post.getWork().getType(),
                post.getWork().getReleaseDate(),
                post.getContent(),
                post.getPostDate()
        );
    }

    public Integer getLikesFromPost(UUID postId) {
        List<PostLikes> likes = postLikesRepository.findByPost(postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found")));

        return likes.size();
    }

    public PostResponse editPost(EditPostRequest request, UUID userId,
                                 UUID postId) {

        Post post = postRepository.findByIdAndUserId(postId, userId)
                        .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Score score = scoreRepository.findByUserAndWork(post.getUser(), post.getWork())
                .orElseThrow(() -> new EntityNotFoundException("Score not found"));

        if (request.score().isPresent()) {
            BigDecimal newScore = request.score().get();
            score.setScore(newScore);
            scoreRepository.save(score);
        }

        post.setContent(request.content());

        Post savedPost = postRepository.save(post);

        List<String> genres = post.getWork().getGenreIds().stream()
                .map(genreCacheService::getTvGenreNameById)
                .collect(Collectors.toList());


        return new PostResponse(
                savedPost.getId(),
                post.getUser().getName(),
                post.getWork().getTitle(),
                post.getWork().getSynopsis(),
                post.getWork().getScore(),
                genres,
                post.getWork().getType(),
                post.getWork().getReleaseDate(),
                savedPost.getContent(),
                LocalDateTime.now()
        );
    }

    public DeletePostResponse deletePost(UUID postId, UUID userId) {
        Post post = postRepository.findByIdAndUserId(postId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        DeletePostResponse response = new DeletePostResponse(
                post.getId(),
                post.getUser().getName(),
                post.getWork().getTitle(),
                post.getContent(),
                "Post deleted successfully"
        );

        postRepository.delete(post);

        return response;
    }

    public List<AllPostsResponse> getAllPostsFromUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Post> posts = postRepository.findAllByUserId(userId);

        return posts.stream()
                .map(this::convertPostToPostResponse)
                .collect(Collectors.toList());
    }

    public List<AllPostsResponse> getTimeline () {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(this::convertPostToPostResponse)
                .collect(Collectors.toList());
    }

    public List<AllPostsResponse> getAllPostsFromAnotherUser (UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        List<Post> posts = postRepository.findAllByUserId(userId);

        return posts.stream()
                .map(this::convertPostToPostResponse)
                .collect(Collectors.toList());
    }
}
