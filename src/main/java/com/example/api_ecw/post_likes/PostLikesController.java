package com.example.api_ecw.post_likes;

import com.example.api_ecw.post_likes.dto.PostLikesResponse;
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
@RequestMapping("/api/post-likes")
@RequiredArgsConstructor
@Tag(name = "Curtidas dos posts", description = "Endpoint para gerenciar likes de posts")
public class PostLikesController {
    private final PosLikesService posLikesService;

    @PostMapping("/{postId}")
    @Operation(summary = "Dê um like no post",
            description = "Dê um like no post, identifique o post pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Like adicionado com sucesso"),
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
                    description = "Usuário já curtiu o post",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Usuário já curtiu o post"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<PostLikesResponse> likeThePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User loggedUser
    ) {
        PostLikesResponse response = posLikesService.likeThePost(loggedUser.getId(), postId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-likes-from-post/{postId}")
    @Operation(summary = "Busca as curtidas do post",
            description = "Obtém o número de curtidas de um post identificado pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filme adicionado à watchlist"),
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
            ),
    })
    public ResponseEntity<Integer> getAllLikesFromPost(
            @PathVariable UUID postId
    ) {
        Integer likes = posLikesService.getAllLikesFromPost(postId);
        return ResponseEntity.status(HttpStatus.OK).body(likes);
    }
}
