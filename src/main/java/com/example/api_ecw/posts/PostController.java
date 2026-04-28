package com.example.api_ecw.posts;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.posts.dto.PostRequest;
import com.example.api_ecw.posts.dto.PostResponse;
import com.example.api_ecw.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
}
