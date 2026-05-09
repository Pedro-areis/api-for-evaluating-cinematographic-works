package com.example.api_ecw.works;

import com.example.api_ecw.infra.config.TokenService;
import com.example.api_ecw.tmdb_api.TmdbIntegrationService;
import com.example.api_ecw.tmdb_api.dto.TmdbSearchResponse;
import com.example.api_ecw.tmdb_api.dto.TmdbWorkResponse;
import com.example.api_ecw.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WorkController.class)
class WorkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TmdbIntegrationService tmdbIntegrationService;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Nested
    class GetWorkByTitle {
        @Test
        @DisplayName("should return 200 when search is successful")
        void shouldReturn200_WhenSearchIsSuccessful() throws Exception {
            // Arrange
            String title = "test";

            TmdbWorkResponse work = new TmdbWorkResponse (
                    1,
                    "originalTitle",
                    "title",
                    "pt-BR",
                    "2020-01-01",
                    "synopsis",
                    "movie",
                    List.of(1,2,3)
            );

            TmdbSearchResponse response = new TmdbSearchResponse(
                    List.of(work),
                    1,
                    10,
                    100
            );
            when(tmdbIntegrationService.getWorkByTitle(title)).thenReturn(response);

            // Act & Assert
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(get("/api/works/search/{title}", title)
                        .with(csrf())
                        .with(user("admin").roles("USER")))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }
    }

}