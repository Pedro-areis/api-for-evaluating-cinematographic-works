package com.example.api_ecw.comment_likes;

import com.example.api_ecw.comment_likes.dto.CommentLikesResponse;
import com.example.api_ecw.user.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment-likes")
@RequiredArgsConstructor
@Tag(name = "Curtidas dos Comentários",
        description = "Gerenciamento das curtidas dos comentários")
public class CommentLikesController {
    private final CommentLikesService commentLikesService;

    @PostMapping("/{commentId}")
    @Operation(summary = "Dê uma curtida no comentário",
            description = "Dê um like no comentário, identifique o comentário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Like adicionado com sucesso"),
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
            ),
            @ApiResponse(responseCode = "409",
                    description = "Usuário já curtiu o comentário",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Usuário já curtiu o comentário"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<CommentLikesResponse> likeTheComment(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal User loggedUser
    ) {
       CommentLikesResponse response = commentLikesService
               .likeTheComment(loggedUser.getId(), commentId);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-likes-from-comment/{commentId}")
    @Operation(summary = "Busca as curtidas do comentário",
            description = "Obtém o número de curtidas de um comentário identificado pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curtidas do comentário encontradas com sucesso"),
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
    public ResponseEntity<Integer> getAllLikesFromComment(
            @PathVariable UUID commentId
    ) {
        Integer likes = commentLikesService.getAllLikesFromComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(likes);
    }
}
