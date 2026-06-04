package com.example.api_ecw.comments;

import com.example.api_ecw.comments.dto.*;
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
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Comentários",
        description = "Endpoint dos comentários")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}/make")
    @Operation(summary = "Faça um comentário",
            description = "Faça um comentário em um post identificado pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comentário realizado com sucesso"),
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
                    description = "Post não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Post não encontrado na base de dados"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<CommentResponse> makeComment (
            @PathVariable UUID postId,
            @AuthenticationPrincipal User loggedUser,
            @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.makeComment(request, postId, loggedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/make-thread/{commentId}")
    @Operation(summary = "Inicie uma thread",
            description = "Responda um cometário e inicie uma thread no comentário pai do post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme adicionado à watchlist"),
            @ApiResponse(responseCode = "404",
                    description = "Comentário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Comentário não encontrado na base de dados"
                                            }"""
                            )
                    )
            ),
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
    public ResponseEntity<CommentResponse> makeThread (
          @PathVariable UUID commentId,
          @AuthenticationPrincipal User loggedUser,
          @Valid @RequestBody CommentRequest request
    ) {
        CommentResponse response = commentService.makeThread(request, commentId, loggedUser.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("thread/{commentId}")
    @Operation(summary = "Veja a thread do comentário",
            description = "Busque a thread do comentário, identifique pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Thread encontrada com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Comentário não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Comentário não encontrado na base de dados"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<AllCommentsResponse> getThread (
            @PathVariable UUID commentId
    ) {
        AllCommentsResponse response = commentService.getAllThreadsFromComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("comments-from-post/{postId}")
    @Operation(summary = "Veja os comentários do post",
            description = "Busque todos os comentários de um post, identifique pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentários encontrados com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Post não encontrado na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Post não encontrado na base de dados"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<AllCommentsResponse> getCommentsFromPost (
            @PathVariable UUID postId
    ) {
        AllCommentsResponse response = commentService.getAllComments(postId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
