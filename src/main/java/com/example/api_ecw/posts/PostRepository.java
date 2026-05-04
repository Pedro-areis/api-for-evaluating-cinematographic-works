package com.example.api_ecw.posts;

import com.example.api_ecw.user.User;
import com.example.api_ecw.works.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByWorkId(UUID workId);
    Optional<Post> findByIdAndUserId(UUID postId, UUID authorId);
    List<Post> findAllByUserId(UUID userId);
    boolean existsByUserAndWork(User user, Work work);
}
