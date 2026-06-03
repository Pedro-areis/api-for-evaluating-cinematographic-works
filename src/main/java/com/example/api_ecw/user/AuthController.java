package com.example.api_ecw.user;

import com.example.api_ecw.infra.config.TokenService;
import com.example.api_ecw.user.dto.LoginRequest;
import com.example.api_ecw.user.dto.StandardErrorDTO;
import com.example.api_ecw.user.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação de usuário",
description = "Endpoint para autenticação de usuário já existente na base de dados")
public class AuthController {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(summary = "Login de usuário",
    description = "Valida o email e senha do usuário e gera um token de autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso"),
            @ApiResponse(responseCode = "401",
                    description = "Credenciais inválidas",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = StandardErrorDTO.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status" : "401",
                                              "error" : "UNAUTHORIZED",
                                              "message" : "E-mail ou senha inválidos"
                                            }"""
                            )
                    )
            )
    })
    public ResponseEntity<TokenResponse> login(
           @Valid @RequestBody LoginRequest loginRequest
    ) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        var authentication = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) Objects.requireNonNull(authentication.getPrincipal()));

        return ResponseEntity.ok(new TokenResponse(token));
    }
}
