package com.example.api_ecw.works;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkRepository extends JpaRepository<Work, UUID> {
    Optional<Work> findById(UUID id);
}
