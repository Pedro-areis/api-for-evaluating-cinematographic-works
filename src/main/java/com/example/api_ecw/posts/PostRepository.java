package com.example.api_ecw.posts;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByWorkId(UUID workId);
    Optional<Post> findByIdAndUserId(UUID postId, UUID authorId);
}
