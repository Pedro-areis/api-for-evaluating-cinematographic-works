package com.example.api_ecw.user;

import com.example.api_ecw.user.dto.UserRequest;
import com.example.api_ecw.user.dto.UserResponse;
import com.example.api_ecw.user.dto.UserUpdate;
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
public class UserController {
    private final UserService userService;

    // Create a new User
    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserRequest request
    ) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update an existing User
    @PatchMapping("/update/{id}")
    public  ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdate updated,
            @AuthenticationPrincipal User user
    ){
        if (!user.getId().equals(id)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserResponse response = userService.updateUser(id, updated);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Delete an existing User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable UUID id,
            @AuthenticationPrincipal User user
    ){

        if (user.getId().equals(id)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String message = userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
