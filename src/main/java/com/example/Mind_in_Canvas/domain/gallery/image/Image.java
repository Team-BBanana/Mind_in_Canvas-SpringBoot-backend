package com.example.Mind_in_Canvas.domain.gallery.image;

import jakarta.persistence.*;
import lombok.Getter;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "image")
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID imageId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String promptUsed;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setIsDeleted(boolean b) {
        isDeleted = b;
    }
    
    public static Image createImage(String generatedBackground, String promptUsed) {
        Image image = new Image();
        image.imageUrl = generatedBackground;
        image.promptUsed = promptUsed;
        return image;
    }

}
