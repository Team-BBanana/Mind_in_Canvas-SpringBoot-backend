package com.example.Mind_in_Canvas.domain.gallery.image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    String findImageUrlByImageId(@Param("imageId") UUID ImageId);

    Image findImageByImageId(UUID imageId);
}
