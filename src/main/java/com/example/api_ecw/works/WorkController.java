package com.example.api_ecw.works;

import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.tmdb_api.dto.TmdbSearchResponse;
import com.example.api_ecw.works.dto.WorkRequest;
import com.example.api_ecw.works.dto.WorkResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
public class WorkController {
    private final TmdbIntegrationService tmdbIntegrationService;
    private final WorkService workService;

    @GetMapping("/search/{title}")
    public ResponseEntity<TmdbSearchResponse> getWorkByTitle(@PathVariable String title) {
        TmdbSearchResponse response = tmdbIntegrationService.getWorkByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/new")
    public ResponseEntity<WorkResponse> createWork(
            @Valid
            @RequestBody WorkRequest work
    ) {
        WorkResponse response = workService.create(work);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
