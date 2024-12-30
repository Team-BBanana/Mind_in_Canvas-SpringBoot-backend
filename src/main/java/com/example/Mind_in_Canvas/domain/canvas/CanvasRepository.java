package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.gallery.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CanvasRepository extends JpaRepository<Canvas, UUID> {
    Image findImageByCanvasId(@Param("canvasId") UUID canvasId);
}