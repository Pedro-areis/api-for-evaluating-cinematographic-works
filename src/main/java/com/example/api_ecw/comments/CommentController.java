package com.example.api_ecw.comments;

import com.example.api_ecw.comments.dto.AllCommentsFromPost;
import com.example.api_ecw.comments.dto.CommentRequest;
import com.example.api_ecw.comments.dto.CommentResponse;
import com.example.api_ecw.comments.dto.ThreadResponse;
import com.example.api_ecw.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/make")
    public ResponseEntity<CommentResponse> makeComment (
            @PathVariable UUID postId,
            @AuthenticationPrincipal User loggedUser,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.makeComment(request, postId, loggedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/make-thread/{commentId}")
    public ResponseEntity<CommentResponse> makeThread (
          @PathVariable UUID commentId,
          @AuthenticationPrincipal User loggedUser,
          @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.makeThread(request, commentId, loggedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("thread/{commentId}")
    public ResponseEntity<ThreadResponse> getThread (
            @PathVariable UUID commentId
    ) {
        ThreadResponse response = commentService.getAllThreadsFromComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("comments-from-post/{postId}")
    public ResponseEntity<AllCommentsFromPost> getCommentsFromPost (
            @PathVariable UUID postId
    ) {
        AllCommentsFromPost response = commentService.getAllComments(postId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
