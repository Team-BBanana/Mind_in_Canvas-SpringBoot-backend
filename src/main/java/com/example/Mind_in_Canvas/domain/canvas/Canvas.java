package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.enums.CanvasStatus;
import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import com.example.Mind_in_Canvas.domain.user.parent.User;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "canvas")
@Getter
@NoArgsConstructor
public class Canvas {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID canvasId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id", columnDefinition = "BINARY(16)")
    private Kid kid;

    private String title;

    @Enumerated(EnumType.STRING)
    private CanvasStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
    public Canvas(Kid kid, String title) {
        this.kid = kid;
        this.title = title;
        this.status = CanvasStatus.NEW;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
