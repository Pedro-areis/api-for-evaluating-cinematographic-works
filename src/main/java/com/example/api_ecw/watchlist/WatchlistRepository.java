package com.example.api_ecw.watchlist;

import com.example.api_ecw.user.User;
import com.example.api_ecw.works.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {
    boolean existsByUserAndWork(User user, Work work);
    List<Watchlist> findAllByUserId(UUID userId);
}
