package com.example.api_ecw.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Nested
    class loadUserByUsername {
        @Test
        @DisplayName("Should load user by username with success")
        void shouldLoadUserByUsernameWithSuccess() {
            // Arrange
            String email = "teste@gmail.com";
            User user = new User();

            when(userRepository.findByEmail(email))
                    .thenReturn(Optional.of(user));

            // Act
            User result = authService.loadUserByUsername(email);

            // Assert
            assertEquals(user, result);
            verify(userRepository, times(1)).findByEmail(email);
        }

        @Test
        @DisplayName("Should return exception when user not found")
        void shouldReturnExceptionWhenUserNotFound () {
            // Arrange
            String email = "teste@gmail.com";
            when(userRepository.findByEmail(email))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () ->
                    authService.loadUserByUsername(email));

            verify(userRepository, times(1)).findByEmail(email);
        }
    }
}