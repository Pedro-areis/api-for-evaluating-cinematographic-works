package com.example.api_ecw.user;

import com.example.api_ecw.infra.config.SecurityConfig;
import com.example.api_ecw.infra.config.TokenService;
import com.example.api_ecw.user.dto.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Nested
    class Login {
        @Test
        @DisplayName("Should return 200 with login success")
        void shouldReturn200_WithLoginSuccess() throws Exception {
            // Arrange
            LoginRequest loginRequest = new LoginRequest(
                    "email",
                    "password"
            );

            User mockUser = new User();
            mockUser.setId(UUID.randomUUID());
            mockUser.setEmail("email");
            mockUser.setPassword("password");

            Authentication authMock = mock(Authentication.class);
            when(authenticationManager.authenticate(any())).thenReturn(authMock);
            when(authMock.getPrincipal()).thenReturn(mockUser);
            when(tokenService.generateToken(mockUser)).thenReturn("token");

            // Act & Assert
            mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token"));

        }

        @Test
        @DisplayName("Should return 400 when login request invalid")
        void shouldReturn400_WhenLoginRequestInvalid() throws Exception {
            // Arrange
            LoginRequest loginRequest = new LoginRequest(
                    "email",
                    ""
            );
            // Act & Assert
            mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 401 when invalid credentials")
        void shouldReturn401_WhenInvalidCredentials() throws Exception {
            // Arrange
            LoginRequest loginRequest = new LoginRequest(
                    "email",
                    "password"
            );
            when(authenticationManager.authenticate(any()))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            // Act & Assert
            mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized());
        }
    }
}