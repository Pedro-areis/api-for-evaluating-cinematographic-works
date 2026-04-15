package com.example.api_ecw.user;

import com.example.api_ecw.user.dto.UserRequest;
import com.example.api_ecw.user.dto.UserUpdate;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    @Nested
    class updateUser {
        @Test
        @DisplayName("Should update user with success when all fields are provided")
        void shouldUpdateUserWithSuccessWhenAllFieldsAreProvided() {
            // Arrange
            User user = new User(
                    UUID.randomUUID(),
                    "teste",
                    "teste@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01"),
                    null
            );
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));

            doReturn(user).when(userRepository).save(any());

            var input = new UserUpdate(
                    "name updated",
                    "teste_updated@gmail.com",
                    "12345678",
                    LocalDate.parse("1990-01-01")
            );
            doReturn("12345678").when(passwordEncoder).encode("12345678");

            // Act
            var result = userService.updateUser(user.getId(), input);

            // Assert
            assertEquals("name updated", result.name());
            assertEquals("teste_updated@gmail.com", result.email());
            assertEquals(LocalDate.parse("1990-01-01"), result.dateBirth());
        }

        @Test
        @DisplayName("Should update user with success when some fields are provided")
        void shouldUpdateUserWithSuccessWhenSomeFieldsAreProvided() {
            // Arrange
            User user = new User(
                    UUID.randomUUID(),
                    "teste",
                    "teste@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01"),
                    null
            );
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));

            doReturn(user).when(userRepository).save(any());

            var input = new UserUpdate(
                    null,
                    "teste_updated@gmail.com",
                    "12345678",
                    null
            );
            doReturn("12345678").when(passwordEncoder).encode("12345678");

            // Act
            var result = userService.updateUser(user.getId(), input);

            // Assert
            assertEquals("teste_updated@gmail.com", result.email());
        }

        @Test
        @DisplayName("Should return exception when user not found")
        void shouldReturnExceptionWhenUserNotFound() {
            // Arrange
            var input = new UserUpdate(
                    "name",
                    "teste@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01")
            );

            UUID id = UUID.randomUUID();
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(EntityNotFoundException.class,
                    () -> userService.updateUser(id, input));
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should return exception when email already exists")
        void shouldReturnExceptionWhenEmailAlreadyExists() {
            // Arrange
            User user = new User(
                    UUID.randomUUID(),
                    "teste",
                    "oldEmail@gmail.com",
                    "123456",
                    LocalDate.parse("1990-01-01"),
                    null
            );
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));

            var input = new UserUpdate(
                    null,
                    "teste@gmail.com",
                    null,
                    null
            );
            when(userRepository.findByEmail("teste@gmail.com"))
                    .thenReturn(Optional.of(new User()));

            // Act & Assert
            assertThrows(DataIntegrityViolationException.class,
                    () -> userService.updateUser(user.getId(), input));
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    class deleteUser {
        @Test
        @DisplayName("Should delete user with success")
        void shouldDeleteUserWithSuccess() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(userRepository.findById(id))
                    .thenReturn(Optional.of(new User()));

            // Act & Assert
            userService.deleteUser(id);
        }

        @Test
        @DisplayName("Should return exception when user not found")
        void shouldReturnExceptionWhenUserNotFound() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(userRepository.findById(id))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> userService.deleteUser(id));
            verify(userRepository, never()).delete(any());
        }
    }

}