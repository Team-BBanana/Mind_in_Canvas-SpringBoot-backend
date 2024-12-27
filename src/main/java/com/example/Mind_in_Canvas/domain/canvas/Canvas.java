package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.user.Kid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "canvas")
@Getter @Setter
public class Canvas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long canvasId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kid_id", nullable = false)
    private Kid kid;

    private String title;

    @Enumerated(EnumType.STRING)
    private CanvasStatus status = CanvasStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum CanvasStatus {
        ACTIVE, COMPLETED
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
}
