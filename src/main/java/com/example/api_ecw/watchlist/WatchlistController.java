package com.example.api_ecw.watchlist;

import com.example.api_ecw.user.User;
import com.example.api_ecw.user.dto.StandardErrorDTO;
import com.example.api_ecw.watchlist.dto.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
@Tag(name = "Watchlist", description = "Endpoint para gerenciar a watchlist de um usuário")
public class WatchlistController {
    private final WatchlistService watchlistService;

    @PostMapping("/add-movie/{tmdbId}")
    @Operation(summary = "Adiciona um filme à watchlist",
            description = "Adiciona um filme à watchlist do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Filme adicionado à watchlist"),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Usuário não encontrado na base de dados."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "Filme já existe na watchlist",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Filme já existe na watchlist do usuário logado"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<WatchlistResponse> addMovieToWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Integer tmdbId
    ) {
        WatchlistResponse response = watchlistService.addMovieToWatchlist(loggedUser.getId(), tmdbId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/add-tv/{tmdbId}")
    @Operation(summary = "Adiciona uma série à watchlist",
            description = "Adiciona uma série à watchlist do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Série adicionado à watchlist"),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Usuário não encontrado na base de dados."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "Série já existe na watchlist",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Série já existe na watchlist do usuário logado"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<WatchlistResponse> addTvToWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Integer tmdbId
    ) {
        WatchlistResponse response = watchlistService.addTvToWatchlist(loggedUser.getId(), tmdbId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my-watchlist")
    @Operation(summary = "Minha Watchlist",
            description = "Obtém a watchlist do usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Watchlist do usuário autenticado"),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Usuário não encontrado na base de dados."
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<List<AllWatchlistResponse>> getWatchlist(
            @AuthenticationPrincipal User loggedUser
    ) {
        List<AllWatchlistResponse> watchlist = watchlistService.getAllWorksFromWatchlist(loggedUser.getId());

        return ResponseEntity.status(HttpStatus.OK).body(watchlist);
    }

    @PatchMapping("update-status-from/{workId}")
    @Operation(summary = "Atualiza o status da obra na watchlist",
            description = "Define o status da obra como Watched e permite o usuário que dê uma nota a obra.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da obra atualizado na watchlist"),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Usuário não encontrado na base de dados."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Obra não encontrada na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
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
            @ApiResponse(responseCode = "400",
                    description = "Watchlist não encontrada na base de dados ou não pertence ao usuário logado",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "BAD_REQUEST",
                                              "message" : "Watchlist não encontrada na base de dados ou não pertence ao usuário logado"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "Obra já está marcada como 'watched' na watchlist do usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Obra já está marcada como 'watched' na watchlist do usuário"
                                            }"""
                            )
                    )
            ),
    })
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
    @Operation(summary = "Excluir obra da watchlist",
            description = "Exclui obra da watchlist do usuário através do WorkId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obra excluída da watchlist do usuário"),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Usuário não encontrado na base de dados."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Obra não encontrada na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
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
            @ApiResponse(responseCode = "400",
                    description = "Watchlist não encontrada na base de dados ou não pertence ao usuário logado",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "BAD_REQUEST",
                                              "message" : "Watchlist não encontrada na base de dados ou não pertence ao usuário logado"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<RemoveWorkFromWatchlist> deleteWorkFromWatchlist(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable UUID workId
    ) {
        RemoveWorkFromWatchlist response = watchlistService.removeWork(loggedUser.getId(), workId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
