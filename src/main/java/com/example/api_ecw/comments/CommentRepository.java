package com.example.api_ecw.comments;

import com.example.api_ecw.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByParentComment(Comment parentComment);
    List<Comment> findByPostAndParentCommentIsNull(Post post);
}
