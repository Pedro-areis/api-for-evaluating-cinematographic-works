package com.example.api_ecw.user;

import com.example.api_ecw.infra.config.TokenService;
import com.example.api_ecw.user.dto.UserRequest;
import com.example.api_ecw.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

            // Act & Assert
        }

    }
}