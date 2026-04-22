package com.example.api_ecw.works;

import com.example.api_ecw.enums.WorkType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "works")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String synopsis;

    private Float score;

    @Enumerated(EnumType.STRING)
    private WorkType type;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "genre_ids", columnDefinition = "integer[]", nullable = false)
    private List<Integer> genreIds;

    @Column(name = "tmdb_id", nullable = false)
    private UUID tmdbId;
}
