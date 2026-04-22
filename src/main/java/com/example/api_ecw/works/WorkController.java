package com.example.api_ecw.works;

import com.example.api_ecw.works.dto.TmdbSearchResponse;
import com.example.api_ecw.works.dto.TmdbWorkResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
public class WorkController {
    private final TmdbIntegrationService tmdbIntegrationService;

    @GetMapping("/search/{title}")
    public TmdbSearchResponse getWorkByTitle(@PathVariable String title) {
        return tmdbIntegrationService.getWorkByTitle(title);
    }
}
