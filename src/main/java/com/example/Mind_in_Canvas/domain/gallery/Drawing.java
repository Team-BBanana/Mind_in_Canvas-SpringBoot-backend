package com.example.Mind_in_Canvas.domain.gallery;

import com.example.Mind_in_Canvas.domain.canvas.Canvas;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "drawing")
@Getter @Setter
public class Drawing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long drawingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canvas_id", nullable = false)
    private Canvas canvas;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image image;

    private String drawingName;

    @Column(columnDefinition = "TEXT")
    private String originalImage;

    @Column(columnDefinition = "TEXT")
    private String finalImage;

    private Float positionX;
    private Float positionY;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}