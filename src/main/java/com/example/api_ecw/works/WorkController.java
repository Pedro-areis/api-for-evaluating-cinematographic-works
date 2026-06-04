package com.example.api_ecw.works;

import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.tmdb_api.dto.TmdbSearchResponse;
import com.example.api_ecw.user.dto.StandardErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
@Tag(name = "Obras", description = "Endpoint para obtenção de obras")
public class WorkController {
    private final TmdbIntegrationService tmdbIntegrationService;

    @GetMapping("/search/{title}")
    @Operation(summary = "Obtém obras por título",
            description = "Obtém obras por título na API do TMDb")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obras obtidas com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Obras não encontradas na API externa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    """
                                    {
                                        "status": "404",
                                        "error": "BAD_REQUEST",
                                        "message": "Obras não encontradas na API externa"
                                    }"""
                            )
                    )
            )
    })
    public ResponseEntity<TmdbSearchResponse> getWorkByTitle(@PathVariable String title) {
        TmdbSearchResponse response = tmdbIntegrationService.getWorkByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
