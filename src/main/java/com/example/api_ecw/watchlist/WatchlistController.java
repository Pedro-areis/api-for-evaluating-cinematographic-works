package com.example.api_ecw.watchlist;

import com.example.api_ecw.user.User;
import com.example.api_ecw.watchlist.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {
    private final WatchlistService watchlistService;

    @PostMapping("/add-movie/{tmdbId}")
    public ResponseEntity<WatchlistResponse> addMovieToWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Integer tmdbId
    ) {
        WatchlistResponse response = watchlistService.addMovieToWatchlist(loggedUser.getId(), tmdbId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-tv/{tmdbId}")
    public ResponseEntity<WatchlistResponse> addTvToWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Integer tmdbId
    ) {
        WatchlistResponse response = watchlistService.addTvToWatchlist(loggedUser.getId(), tmdbId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-watchlist")
    public ResponseEntity<List<AllWatchlistResponse>> getWatchlist(
            @AuthenticationPrincipal User loggedUser
    ) {
        List<AllWatchlistResponse> watchlist = watchlistService.getAllWorksFromWatchlist(loggedUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(watchlist);
    }

    @PatchMapping("update-status-from/{workId}")
    public ResponseEntity<WatchlistUpdated> updateStatusForWatched(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID workId,
            @RequestBody WatchlistUpdatedRequest request
    ) {
        WatchlistUpdated response = watchlistService
                .updateStatusForWatched(request.workScore(), loggedUser.getId(), workId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("delete-from-watchlist/{workId}")
    public ResponseEntity<RemoveWorkFromWatchlist> deleteWorkFromWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID workId
    ) {
        RemoveWorkFromWatchlist response = watchlistService.removeWork(loggedUser.getId(), workId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
