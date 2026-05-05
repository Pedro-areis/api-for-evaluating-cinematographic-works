package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.user.User;
import com.example.api_ecw.works.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {
    boolean existsByUserAndWorkAndType(User user, Work work, WorkType type);
    List<Watchlist> findAllByUserId(UUID userId);
    Optional<Watchlist> findByUserIdAndWorkId(UUID userId, UUID workId);
}
