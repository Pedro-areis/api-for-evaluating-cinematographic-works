package com.example.api_ecw.user;

import com.example.api_ecw.user.dto.StandardErrorDTO;
import com.example.api_ecw.user.dto.UserRequest;
import com.example.api_ecw.user.dto.UserResponse;
import com.example.api_ecw.user.dto.UserUpdate;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

// Controller for User
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UserController {
    private final UserService userService;

    // Create a new User
    @PostMapping("/register")
    @Operation(summary = "Criar um novo usuário",
            description = "Cria um novo usuário com os dados enviados no body da requisição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro na criação do usuário (ex: campos obrigatórios vazios)",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "400",
                                              "error" : "BAD_REQUEST",
                                              "message" : "O campo 'email' não pode ser vazio."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "E-mail já existente na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Já existe um usuário cadastrado com o e-mail informado."
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update an existing User
    @PatchMapping("/update-me")
    @Operation(summary = "Atualiza os dados do usuário",
            description = "Atualiza o usuário com os novos dados enviados no body da requisição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Erro na atualização do usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "BAD_REQUEST",
                                              "message" : "O campo 'email' não pode ser vazio."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "409",
                    description = "E-mail já existente na base de dados",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "CONFLICT",
                                              "message" : "Já existe um usuário cadastrado com o e-mail informado."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado",
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
    public  ResponseEntity<UserResponse> updateUser(
            @Valid @RequestBody UserUpdate updated,
            @AuthenticationPrincipal User loggedUser
    ){
        UserResponse response = userService.updateUser(loggedUser.getId(), updated);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete an existing User
    @DeleteMapping("/delete-me")
    @Operation(summary = "Exclui o usuário da base de dados",
            description = "Exclui todos os dados do usuário, junto a todas suas interações com o sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "400",
                    description = "Erro na atualização do usuário",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "409",
                                              "error" : "BAD_REQUEST",
                                              "message" : "O campo 'email' não pode ser vazio."
                                            }"""
                            )
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Usuário não encontrado",
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
    public ResponseEntity<String> deleteUser(
            @AuthenticationPrincipal User loggedUser
    ){
        String message = userService.deleteUser(loggedUser.getId());
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
