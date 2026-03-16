package com.example.api_ecw.works;

import com.example.api_ecw.enums.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "works")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Works {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 255)
    private String synopsis;

    private Float score;

    @Enumerated(EnumType.STRING)
    private WorkType type;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
