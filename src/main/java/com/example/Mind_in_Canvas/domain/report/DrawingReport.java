package com.example.Mind_in_Canvas.domain.report;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.Mind_in_Canvas.domain.gallery.drawing.Drawing;

@Entity
@Table(name = "drawing_report")
@Getter
@Setter
public class DrawingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID DrawingReportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawing_id", nullable = false)
    private Drawing drawing;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}