package com.example.api_ecw.scores;

import com.example.api_ecw.scores.dto.EditScoreRequest;
import com.example.api_ecw.scores.dto.ScoreRequest;
import com.example.api_ecw.scores.dto.ScoreResponse;
import com.example.api_ecw.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
@RequiredArgsConstructor
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping("/give-score/{tmdbId}")
    public ResponseEntity<ScoreResponse> giveScore(
            @PathVariable Integer tmdbId,
            @Valid @RequestBody ScoreRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        ScoreResponse response = scoreService.giveScore(request, loggedUser.getId(), tmdbId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/update-score/{workId}")
    public ResponseEntity<ScoreResponse> updateScore(
            @PathVariable UUID workId,
            @Valid @RequestBody EditScoreRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        ScoreResponse response = scoreService.editScore(request, workId, loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
