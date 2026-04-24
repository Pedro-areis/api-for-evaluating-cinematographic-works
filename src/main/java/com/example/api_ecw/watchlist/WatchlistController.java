package com.example.api_ecw.watchlist;

import com.example.api_ecw.user.User;
import com.example.api_ecw.watchlist.dto.WatchlistResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {
    private final WatchlistService watchlistService;

    @PostMapping("/add-work/{tmdbId}")
    public ResponseEntity<WatchlistResponse> addWorkToWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Integer tmdbId
    ) {
        WatchlistResponse response = watchlistService.addMovieToWatchlist(loggedUser.getId(), tmdbId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
