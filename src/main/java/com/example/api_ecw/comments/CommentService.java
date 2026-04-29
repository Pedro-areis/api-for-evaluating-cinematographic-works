package com.example.api_ecw.comments;

import com.example.api_ecw.comments.dto.CommentRequest;
import com.example.api_ecw.comments.dto.CommentResponse;
import com.example.api_ecw.comments.dto.RepliesDTO;
import com.example.api_ecw.comments.dto.ThreadResponse;
import com.example.api_ecw.posts.Post;
import com.example.api_ecw.posts.PostRepository;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.List;
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
                savedComment.getContent(),
                post.getWork().getTitle(),
                post.getContent(),
                LocalDateTime.now()
        );
    }

    public CommentResponse makeThread(CommentRequest request, UUID commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Comment reply = new Comment();

        reply.setUser(user);
        reply.setPost(comment.getPost());
        reply.setContent(request.content());
        reply.setParentComment(comment);

        Comment savedComment = commentRepository.save(reply);

        return new CommentResponse(
                savedComment.getId(),
                user.getName(),
                savedComment.getContent(),
                comment.getPost().getWork().getTitle(),
                comment.getContent(),
                LocalDateTime.now()
        );
    }
    
    public ThreadResponse getAllThreadsFromComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        List<Comment> replies = commentRepository.findByParentComment(comment);

        List<RepliesDTO> repliesDTO = replies.stream()
                .map(this::convertCommentToThreadResponse)
                .toList();

        return new ThreadResponse (
                comment.getId(),
                comment.getPost().getWork().getTitle(),
                comment.getUser().getName(),
                comment.getContent(),
                repliesDTO
        );
    }

    private RepliesDTO convertCommentToThreadResponse (Comment reply) {
        return new RepliesDTO (
                reply.getId(),
                reply.getUser().getName(),
                reply.getContent()
        );
    }
}
