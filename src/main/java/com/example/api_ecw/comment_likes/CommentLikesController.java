package com.example.api_ecw.comment_likes;

import com.example.api_ecw.comment_likes.dto.CommentLikesResponse;
import com.example.api_ecw.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment-likes")
@RequiredArgsConstructor
public class CommentLikesController {
    private final CommentLikesService commentLikesService;

    @PostMapping("/{commentId}")
    public ResponseEntity<CommentLikesResponse> likeTheComment(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal User loggedUser
    ) {
       CommentLikesResponse response = commentLikesService
               .likeTheComment(loggedUser.getId(), commentId);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-likes/{commentId}")
    public ResponseEntity<Integer> getAllLikesFromComment(
            @PathVariable UUID commentId
    ) {
        Integer likes = commentLikesService.getAllLikesFromComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(likes);
    }
}
