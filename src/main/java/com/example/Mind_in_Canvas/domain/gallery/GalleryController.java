package com.example.Mind_in_Canvas.domain.gallery;

import com.example.Mind_in_Canvas.dto.gallery.AiEnhancementResponse;
import com.example.Mind_in_Canvas.dto.gallery.GalleryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<GalleryResponse> getImage(
            @PathVariable UUID imageId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(galleryService.getImage(imageId));
    }

    @PatchMapping("/{imageId}")
    public ResponseEntity<String> hideImage(
            @PathVariable UUID imageId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return galleryService.hideImage(imageId);
    }

    @PostMapping("/{imageId}/ai")
    public ResponseEntity<AiEnhancementResponse> getEnhancedImage(
            @PathVariable UUID drawingId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(galleryService.getEnhancedImage(drawingId));
    }
}
