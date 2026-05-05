package com.example.api_ecw.works;

import com.example.api_ecw.enums.WorkType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WorkRepository extends JpaRepository<Work, UUID> {
    Optional<Work> findByTmdbIdAndType(Integer tmdbId, WorkType type);
    Optional<Work> findByTmdbId(Integer tmdbId);
}
