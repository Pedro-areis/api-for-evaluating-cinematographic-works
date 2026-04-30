package com.example.api_ecw.post_likes;

import com.example.api_ecw.post_likes.dto.PostLikesResponse;
import com.example.api_ecw.posts.Post;
import com.example.api_ecw.posts.PostRepository;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PosLikesService {
    private final PostLikesRepository postLikesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostLikesResponse likeThePost(UUID userId, UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (postLikesRepository.existsByUser(user)) {
            throw new DataIntegrityViolationException("Post already liked by user");
        }

        PostLikes like = new PostLikes();

        like.setUser(user);
        like.setPost(post);

        PostLikes savedLike = postLikesRepository.save(like);

        return new PostLikesResponse(
                savedLike.getId(),
                user.getName(),
                LocalDateTime.now()
        );
    }

    public Integer getAllLikesFromPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<PostLikes> likes = postLikesRepository.findByPost(post);

        return likes.size();
    }
}
