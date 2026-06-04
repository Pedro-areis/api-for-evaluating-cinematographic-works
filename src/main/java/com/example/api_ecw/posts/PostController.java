package com.example.api_ecw.posts;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.posts.dto.*;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints relacionados a posts")
public class PostController {
    private final PostService postService;

    @PostMapping("/new-post/{tmdbId}")
    @Operation(summary = "Cria um novo post",
            description = "Cria um novo post com base em uma obra (filme ou série)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post criado com sucesso"),
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
                    description = "Usuário já realizou um post para essa obra",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Usuário já realizou um post para essa obra"
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Avaliação da obra não encontrada na base de dados do usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Avaliação da obra não encontrada na base de dados do usuário"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<PostResponse> createPost(
            @PathVariable Integer tmdbId,
            @RequestBody @Valid PostRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        PostResponse response;
        if (request.type() == WorkType.movie) {
            response = postService.createPostFromMovie(request, loggedUser.getId(), tmdbId);
        } else {
            response = postService.createPostFromTv(request, loggedUser.getId(), tmdbId);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/all-posts-from-work/{workId}")
    @Operation(summary = "Busque todas os posts de uma obra",
            description = "Busque todas os posts de uma determinada obra através do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts encontrados com sucesso"),
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
            )
    })
    public ResponseEntity<List<AllPostsResponse>> getAllPostsFromWork(@PathVariable UUID workId) {
        List<AllPostsResponse> posts = postService.getAllPostsFromWork(workId);
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/edit-post/{postId}")
    @Operation(summary = "Atualize seu post",
            description = "Atualiza seu post da obra pelo seu ID")
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
    })
    public ResponseEntity<PostResponse> editPost(
            @PathVariable UUID postId,
            @RequestBody @Valid EditPostRequest request,
            @AuthenticationPrincipal User loggedUser
    ) {
        PostResponse response = postService.editPost(request, loggedUser.getId(), postId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/delete-post/{postId}")
    @Operation(summary = "Exclua seu post",
            description = "Exclua seu post pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post excluído com sucesso"),
            @ApiResponse(responseCode = "404",
                    description = "Post não encontrado ou não pertence ao usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "404",
                                              "error" : "NOT_FOUND",
                                              "message" : "Post não encontrado ou não pertence ao usuário"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<DeletePostResponse> deletePost(
            @PathVariable UUID postId,
            @AuthenticationPrincipal User loggedUser
    ) {
        DeletePostResponse response = postService.deletePost(postId, loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/all-my-posts")
    @Operation(summary = "Todos os meus posts",
            description = "Busca todos os meus posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos os meus posts encontrados com sucesso"),
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
                                              "message" : "Usuário não encontrado na base de dados"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<List<AllPostsResponse>> getAllMyPosts(
            @AuthenticationPrincipal User loggedUser
    ) {
        List<AllPostsResponse> posts = postService.getAllPostsFromUser(loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/timeline")
    @Operation(summary = "Obtenha a timeline",
            description = "Obtenha a timeline com os posts de todos os usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Timeline encontrada com sucesso")
    })
    public ResponseEntity<List<AllPostsResponse>> getTimeline() {
        List<AllPostsResponse> posts = postService.getTimeline();
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @GetMapping("/all-posts-from-user/{userId}")
    @Operation(summary = "Obtenha todos os posts de um usuário",
            description = "Obtenha todos os posts de um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posts encontrados com sucesso"),
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
                                              "message" : "Usuário não encontrado na base de dados"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<List<AllPostsResponse>> getAllPostsFromUser(
            @PathVariable UUID userId
    ) {
        List<AllPostsResponse> posts = postService.getAllPostsFromAnotherUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }
}
