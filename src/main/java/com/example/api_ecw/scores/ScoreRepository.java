package com.example.api_ecw.scores;

import com.example.api_ecw.user.User;
import com.example.api_ecw.works.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ScoreRepository extends JpaRepository<Score, UUID> {
    boolean existsByUserAndWork(User user, Work work);
    Optional<Score> findById(UUID id);
}
