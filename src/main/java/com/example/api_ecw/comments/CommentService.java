package com.example.api_ecw.comments;

import com.example.api_ecw.comment_likes.CommentLikes;
import com.example.api_ecw.comment_likes.CommentLikesRepository;
import com.example.api_ecw.comments.dto.*;
import com.example.api_ecw.posts.Post;
import com.example.api_ecw.posts.PostRepository;
import com.example.api_ecw.posts.PostService;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentLikesRepository commentLikesRepository;
    private final PostService postService;

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
    
    public AllCommentsResponse getAllThreadsFromComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        List<Comment> replies = commentRepository.findByParentComment(comment);

        List<RepliesDTO> repliesDTO = replies.stream()
                .map(this::convertCommentToThreadResponse)
                .toList();

        return new AllCommentsResponse (
                comment.getId(),
                comment.getUser().getName(),
                comment.getPost().getWork().getTitle(),
                getLikesFromComment(commentId),
                comment.getContent(),
                repliesDTO
        );
    }

    private Integer getLikesFromComment(UUID commentId) {
        List<CommentLikes> likes = commentLikesRepository.findByComment(commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found")));
        return likes.size();
    }

    private RepliesDTO convertCommentToThreadResponse (Comment reply) {
        return new RepliesDTO (
                reply.getId(),
                reply.getUser().getName(),
                getLikesFromComment(reply.getId()),
                reply.getContent()
        );
    }

    public AllCommentsResponse getAllComments (UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<Comment> comments = commentRepository.findByPostAndParentCommentIsNull(post);

        List<RepliesDTO> repliesDTO = comments.stream()
                .map(this::convertCommentToThreadResponse)
                .toList();

        return new AllCommentsResponse (
                post.getId(),
                post.getUser().getName(),
                post.getWork().getTitle(),
                postService.getLikesFromPost(postId),
                post.getContent(),
                repliesDTO
        );

    }
}
