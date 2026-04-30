package com.example.api_ecw.posts;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.posts.dto.DeletePostResponse;
import com.example.api_ecw.posts.dto.EditPostRequest;
import com.example.api_ecw.posts.dto.PostRequest;
import com.example.api_ecw.posts.dto.PostResponse;
import com.example.api_ecw.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/new-post/{tmdbId}")
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Integer tmdbId,
            @RequestBody @Valid PostRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        PostResponse response;
        if (request.type() != WorkType.movie) {
            response = postService.createPostFromTv(request, loggedUser.getId(), tmdbId);
        } else {
            response = postService.createPostFromMovie(request, loggedUser.getId(), tmdbId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-posts-from-work/{workId}")
    public ResponseEntity<List<PostResponse>> getAllPostsFromWork(@PathVariable UUID workId) {
        List<PostResponse> posts = postService.getAllPostsFromWork(workId);
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/edit-post/{postId}")
    public ResponseEntity<PostResponse> editPost(
            @PathVariable UUID postId,
            @RequestBody @Valid EditPostRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        PostResponse response = postService.editPost(request, loggedUser.getId(), postId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<DeletePostResponse> deletePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User loggedUser
    ) {
        DeletePostResponse response = postService.deletePost(postId, loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all-my-posts")
    public ResponseEntity<List<PostResponse>> getAllMyPosts(
            @AuthenticationPrincipal User loggedUser
    ) {
        List<PostResponse> posts = postService.getAllPostsFromUser(loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/timeline")
    public ResponseEntity<List<PostResponse>> getTimeline() {
        List<PostResponse> posts = postService.getTimeline();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
