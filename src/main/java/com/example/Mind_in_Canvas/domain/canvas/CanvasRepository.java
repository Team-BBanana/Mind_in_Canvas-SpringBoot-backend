package com.example.Mind_in_Canvas.domain.canvas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CanvasRepository extends JpaRepository<Canvas, UUID> {
    String findImageUrlByCanvasId(@Param("canvasId") UUID canvasId);
}