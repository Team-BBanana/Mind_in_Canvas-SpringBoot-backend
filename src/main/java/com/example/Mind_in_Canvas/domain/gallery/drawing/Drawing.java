package com.example.Mind_in_Canvas.domain.gallery.drawing;

import com.example.Mind_in_Canvas.domain.canvas.Canvas;
import com.example.Mind_in_Canvas.domain.gallery.image.Image;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "drawing")
@Getter
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID drawingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "canvas_id", columnDefinition = "BINARY(16)")
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