package com.example.api_ecw.post_likes;

import com.example.api_ecw.post_likes.dto.PostLikesResponse;
import com.example.api_ecw.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/post-likes")
@RequiredArgsConstructor
public class PostLikesController {
    private final PosLikesService posLikesService;

    @PostMapping("/{postId}")
    public ResponseEntity<PostLikesResponse> likeThePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User loggedUser
    ) {
        PostLikesResponse response = posLikesService.likeThePost(loggedUser.getId(), postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-likes-from-post/{postId}")
    public ResponseEntity<Integer> getAllLikesFromPost(
            @PathVariable UUID postId
    ) {
        Integer likes = posLikesService.getAllLikesFromPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(likes);
    }
}
