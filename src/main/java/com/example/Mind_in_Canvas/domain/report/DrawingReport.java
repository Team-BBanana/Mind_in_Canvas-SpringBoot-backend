package com.example.Mind_in_Canvas.domain.report;

import com.example.Mind_in_Canvas.domain.gallery.Drawing;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "drawing_report")
@Getter
@Setter
public class DrawingReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long DrawingReportId;

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