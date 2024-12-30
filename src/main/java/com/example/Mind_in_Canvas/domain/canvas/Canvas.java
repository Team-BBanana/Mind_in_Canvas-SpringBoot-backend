package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.enums.CanvasStatus;
import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "canvas")
@Getter 
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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
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


    public static Canvas createCanvas(Kid kid) {
        Canvas canvas = new Canvas();
        canvas.kid = kid;
        canvas.status = CanvasStatus.NEW;
        return canvas;
    }

}
