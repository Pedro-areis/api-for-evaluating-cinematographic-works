package com.example.api_ecw.comment_likes;

import com.example.api_ecw.comments.Comment;
import com.example.api_ecw.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentLikesRepository extends JpaRepository<CommentLikes, UUID> {
    boolean existsByUserAndComment(User user, Comment comment);
    List<CommentLikes> findByComment(Comment comment);
}
