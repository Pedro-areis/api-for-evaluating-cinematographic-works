package com.example.api_ecw.watchlist;

import com.example.api_ecw.enums.WorkStatus;
import com.example.api_ecw.enums.WorkType;
import com.example.api_ecw.user.User;
import com.example.api_ecw.works.Works;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "watchlist",
        uniqueConstraints = {
            @UniqueConstraint(name = "unique_user_work", columnNames = {"user_id", "work_id"}),
            @UniqueConstraint(name = "unique_user_name", columnNames = {"user_id", "name"})
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "work_id", nullable = false)
    private Works work;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkType type;

    @Enumerated(EnumType.STRING)
    private WorkStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
