package com.example.api_ecw.scores;

import com.example.api_ecw.scores.dto.EditScoreRequest;
import com.example.api_ecw.scores.dto.ScoreRequest;
import com.example.api_ecw.scores.dto.ScoreResponse;
import com.example.api_ecw.user.User;
import com.example.api_ecw.user.dto.StandardErrorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Scores", description = "Endpoints para gerenciamento de avaliações")
public class ScoreController {
    private final ScoreService scoreService;

    @PostMapping("/give-score/{tmdbId}")
    @Operation(summary = "Faça uma avaliação",
            description = "Dá uma nota para uma obra assistida através do seu TmdbId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Avaliação realizada com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "404",
                                                "error": "NOT_FOUND",
                                                "message": "Usuário não encontrado na base de dados"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "Avaliação já realizada para a obra",
                    content =  @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples =  @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Avaliação já realizada para a obra"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Requisição inválida ou incompleta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "400",
                                                "error": "BAD_REQUEST",
                                                "message": "Requisição inválida ou incompleta"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ScoreResponse> giveScore(
            @PathVariable Integer tmdbId,
            @Valid @RequestBody ScoreRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        ScoreResponse response = scoreService.giveScore(request, loggedUser.getId(), tmdbId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/update-score/{workId}")
    @Operation(summary = "Atualize sua avaliação",
            description = "Atualiza sua avaliação na obra após uma nova análise")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Obra não encontrada na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Obra não encontrada na base de dados."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "404",
                                                "error": "NOT_FOUND",
                                                "message": "Usuário não encontrado na base de dados"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Avaliação não encontrada na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "404",
                                                "error": "NOT_FOUND",
                                                "message": "Avaliação não encontrada na base de dados"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400",
                    description = "Requisição inválida ou incompleta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "400",
                                                "error": "BAD_REQUEST",
                                                "message": "Requisição inválida ou incompleta"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<ScoreResponse> updateScore(
            @PathVariable UUID workId,
            @Valid @RequestBody EditScoreRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        ScoreResponse response = scoreService.editScore(request, workId, loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
