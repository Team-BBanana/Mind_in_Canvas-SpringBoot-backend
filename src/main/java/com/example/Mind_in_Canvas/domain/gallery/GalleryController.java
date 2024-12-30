package com.example.Mind_in_Canvas.domain.gallery;

import com.example.Mind_in_Canvas.dto.gallery.GalleryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/gallery")
public class GalleryController {

    private final GalleryService galleryService;

    public GalleryController(GalleryService galleryService) {
        this.galleryService = galleryService;
    }

    @GetMapping("/")
    public ResponseEntity<GalleryResponse> getImage(@RequestParam UUID imageId) {
        return ResponseEntity.ok(galleryService.getImage(imageId));
    }

}
