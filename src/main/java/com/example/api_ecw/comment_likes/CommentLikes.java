package com.example.api_ecw.comment_likes;

import com.example.api_ecw.comments.Comment;
import com.example.api_ecw.posts.Post;
import com.example.api_ecw.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posting_likes",
        uniqueConstraints = {
            @UniqueConstraint(name = "unique_user_comment", columnNames = {"user_id", "comment_id"})
        }
)

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @Column(name = "like_date", nullable = false, updatable = false)
    private LocalDateTime likeDate;
}
