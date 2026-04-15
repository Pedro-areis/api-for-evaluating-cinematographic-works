package com.example.api_ecw.user;

import com.example.api_ecw.infra.config.TokenService;
import com.example.api_ecw.user.dto.UserRequest;
import com.example.api_ecw.user.dto.UserResponse;
import com.example.api_ecw.user.dto.UserUpdate;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    UserRepository userRepository;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse(
                "name",
                "email",
                LocalDate.parse("1990-01-01"),
                LocalDateTime.now()
        );
    }

    @Nested
    class createUser {
        @Test
        @DisplayName("Should return 201 when create user with success")
        void shouldReturn201_WhenCreateUserWithSuccess() throws Exception {
            // Arrange
            UserRequest userRequest = new UserRequest(
                    "name",
                    "email",
                    "password",
                    LocalDate.parse("1990-01-01")
            );
            when(userService.createUser(any())).thenReturn(userResponse);

            // Act & Assert
            mockMvc.perform(post("/api/users/register")
                            .with(csrf())
                            .with(user("admin").roles("USER"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value(userResponse.name()))
                    .andExpect(jsonPath("$.email").value(userResponse.email()))
                    .andExpect(jsonPath("$.dateBirth").value(userResponse.dateBirth().toString()))
                    .andExpect(jsonPath("$.createdAt").exists());
        }

        @Test
        @DisplayName("Should return 400 when request is not complete")
        void shouldReturn400_WhenRequestIsNotComplete() throws  Exception {
            // Arrange
            UserRequest invalidRequest = new UserRequest(
                    "name",
                    "", // invalid email
                    "password",
                    LocalDate.parse("1990-01-01")
            );

            // Act & Assert
            mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .with(user("admin").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class updateUser {
        @Test
        @DisplayName("Should return 200 when update user with success")
        void shouldReturn200_WhenUpdateUserWithSuccess() throws Exception {
            // Arrange
            UUID userId = UUID.randomUUID();
            User user = new User();
            user.setId(userId);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());

            UserUpdate userUpdate = new UserUpdate(
                    null,
                    "email",
                    "password",
                    null
            );
            when(userService.updateUser(userId, userUpdate)).thenReturn(userResponse);

            // Act & Assert
            mockMvc.perform(patch("/api/users/update/{id}", userId)
                        .with(csrf())
                        .with(authentication(authToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value(userResponse.name()))
                    .andExpect(jsonPath("$.email").value(userResponse.email()))
                    .andExpect(jsonPath("$.dateBirth").value(userResponse.dateBirth().toString()))
                    .andExpect(jsonPath("$.createdAt").exists());
        }

        @Test
        @DisplayName("Should return 401 when user id different of id logged")
        void shouldReturn401_WhenUserIdDifferentOfIdLogged() throws Exception {
            // Arrange
            UUID loggedId = UUID.randomUUID();
            User user = new User();
            user.setId(loggedId);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());

            UUID otherId = UUID.randomUUID();

            UserUpdate userUpdate = new UserUpdate(
                    null,
                    "email",
                    "password",
                    null
            );

            // Act & Assert
            mockMvc.perform(patch("/api/users/update/{id}", otherId)
                        .with(csrf())
                        .with(authentication(authToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    class deleteUser {
        @Test
        @DisplayName("Should return 200 when delete user with success")
        void shouldReturn200_WhenDeleteUserWithSuccess() throws  Exception {
            // Arrange
            UUID userId = UUID.randomUUID();

            User user = new User();
            user.setId(userId);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());

            when(userService.deleteUser(userId)).thenReturn("User deleted successfully");

            // Act & Assert
            mockMvc.perform(delete("/api/users/delete/{id}", userId)
                        .with(csrf())
                        .with(authentication(authToken)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return 404 when user does not exist")
        void shouldReturn404_WhenUserDoesNotExist() throws Exception{
            // Arrange
            UUID userId = UUID.randomUUID();

            User user = new User();
            user.setId(userId);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());

            doThrow(new EntityNotFoundException("Usuário não encontrado"))
                    .when(userService).deleteUser(userId);

            // Act & Assert
            mockMvc.perform(delete("/api/users/delete/{id}", userId)
                        .with(csrf())
                        .with(authentication(authToken)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 401 when user id different of id logged")
        void shouldReturn401_WhenUserIdDifferentOfIdLogged() throws Exception {
            // Arrange
            UUID loggedId = UUID.randomUUID();
            UUID otherId = UUID.randomUUID();

            User user = new User();
            user.setId(loggedId);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, List.of());

            when(userService.deleteUser(otherId)).thenThrow(new RuntimeException());

            // Act & Assert
            mockMvc.perform(delete("/api/users/delete/{id}", otherId)
                        .with(csrf())
                        .with(authentication(authToken)))
                    .andExpect(status().isUnauthorized());
        }
    }
}