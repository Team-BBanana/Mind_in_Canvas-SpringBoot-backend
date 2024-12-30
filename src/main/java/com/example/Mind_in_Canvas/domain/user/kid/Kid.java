package com.example.Mind_in_Canvas.domain.user.kid;

import com.example.Mind_in_Canvas.domain.user.parent.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "kid")
@Getter
@Setter
@NoArgsConstructor
public class Kid {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID kidId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    private User parent;

    @Column(nullable = false)
    private String name;

    private Integer age;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Kid(User parent, String kidName, int kidAge) {
        // 엄마 등록
        this.parent = parent;
        this.name = kidName;
        this.age = kidAge;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @Builder
    public Kid(UUID kidId, User parent, String name, Integer age, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.kidId = kidId;
        this.parent = parent;
        this.name = name;
        this.age = age;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
