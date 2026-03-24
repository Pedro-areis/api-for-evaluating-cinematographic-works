package com.example.api_ecw.user;

import com.example.api_ecw.user.dto.UserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUser() {
            // Arrange
            var user = new User(
                    UUID.randomUUID(),
                    "teste",
                    "teste@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01"),
                    null
            );
            doReturn(user).when(userRepository).save(any());

            var input = new UserRequest(
                    "teste",
                    "teste@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01")
                    );
            doReturn("123456").when(passwordEncoder).encode("123456");

            // Act
            var result = userService.createUser(input);

            // Assert
            verify(userRepository, times(1)).save(any());
            assertNotNull(result);
        }

        @Test
        @DisplayName("Should throw exception when Email already exists")
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            // Arrange
            var input = new UserRequest(
                    "name",
                    "teste@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01")
            );

            when(userRepository.existsByEmail("teste@gmail.com")).thenReturn(true);

            // Act & Assert
            assertThrows(DataIntegrityViolationException.class, () ->
                    userService.createUser(input));
            verify(userRepository, never()).save(any());
        }
    }

}