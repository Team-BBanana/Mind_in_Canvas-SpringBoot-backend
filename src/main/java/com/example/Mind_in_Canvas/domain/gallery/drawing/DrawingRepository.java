package com.example.Mind_in_Canvas.domain.gallery.drawing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, UUID> {
    String findImageUrlByCanvas_CanvasId(@Param("canvasId") UUID canvasId);
}