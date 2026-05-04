package com.example.api_ecw.comment_likes;

import com.example.api_ecw.comment_likes.dto.CommentLikesResponse;
import com.example.api_ecw.comments.Comment;
import com.example.api_ecw.comments.CommentRepository;
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
public class CommentLikesService {
    private final CommentLikesRepository commentLikesRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public CommentLikesResponse likeTheComment(UUID userId, UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (commentLikesRepository.existsByUserAndComment(user, comment)) {
            throw new DataIntegrityViolationException("Comment already liked by user");
        }

        CommentLikes like = new  CommentLikes();

        like.setUser(user);
        like.setComment(comment);

        CommentLikes savedLike = commentLikesRepository.save(like);

        return new CommentLikesResponse(
                savedLike.getId(),
                user.getName(),
                LocalDateTime.now()
        );
    }

    public Integer getAllLikesFromComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        List<CommentLikes> likes = commentLikesRepository.findByComment(comment);

        return likes.size();
    }
}
