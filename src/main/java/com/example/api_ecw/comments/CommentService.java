package com.example.api_ecw.comments;

import com.example.api_ecw.comments.dto.CommentRequest;
import com.example.api_ecw.comments.dto.CommentResponse;
import com.example.api_ecw.posts.Post;
import com.example.api_ecw.posts.PostRepository;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentResponse makeComment(CommentRequest request, UUID postId, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment newComment = new Comment();

        newComment.setUser(user);
        newComment.setPost(post);
        newComment.setContent(request.content());

        Comment savedComment = commentRepository.save(newComment);

        String workName = post.getWork().getTitle();

        return new CommentResponse(
                savedComment.getId(),
                user.getName(),
                workName,
                post.getContent(),
                LocalDateTime.now()
        );
    }
}
