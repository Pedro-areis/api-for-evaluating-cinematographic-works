package com.example.api_ecw.post_likes;

import com.example.api_ecw.posts.Post;
import com.example.api_ecw.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostLikesRepository extends JpaRepository<PostLikes, UUID> {
    List<PostLikes> findByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
}
