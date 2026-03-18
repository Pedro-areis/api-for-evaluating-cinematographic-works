package com.example.api_ecw.user;

import com.example.api_ecw.user.dto.UserRequest;
import com.example.api_ecw.user.dto.UserResponse;
import com.example.api_ecw.user.dto.UserUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

// Service for User
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Create a new User
    public UserResponse createUser(UserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setName(request.name());
        newUser.setEmail(request.email());
        newUser.setPasswordHash(request.passwordHash());
        newUser.setDateBirth(request.dateBirth());

        User savedUser = userRepository.save(newUser);

        return new UserResponse(
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPasswordHash(),
                savedUser.getDateBirth(),
                savedUser.getCreatedAt()
        );
    }

    // Update an existing User
    public UserResponse updateUser(UUID id, UserUpdate updated){
        User userOwner = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updated.email() != null && !updated.email().isBlank()) {
            if (userRepository.findByEmail(updated.email()).isPresent()) {
                throw new RuntimeException("Email already exists");
            }
            userOwner.setEmail(updated.email());
        }

        if(updated.name() != null && !updated.name().isBlank()) {
            userOwner.setName(updated.name());
        }

        if(updated.dateBirth() != null) {
            userOwner.setDateBirth(updated.dateBirth());
        }

        if(updated.passwordHash() != null && !updated.passwordHash().isBlank()) {
            userOwner.setPasswordHash(updated.passwordHash());
        }

        User userUpdated = userRepository.save(userOwner);

        return new UserResponse(
                userUpdated.getName(),
                userUpdated.getEmail(),
                userUpdated.getPasswordHash(),
                userUpdated.getDateBirth(),
                userUpdated.getCreatedAt()
        );
    }

    // Delete an existing User
    public String deleteUser(UUID id){
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(userToDelete);

        return  "User has been deleted";
    }
}
