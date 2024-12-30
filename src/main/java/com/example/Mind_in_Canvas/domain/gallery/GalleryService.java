package com.example.Mind_in_Canvas.domain.gallery;

import com.example.Mind_in_Canvas.domain.gallery.drawing.DrawingRepository;
import com.example.Mind_in_Canvas.domain.gallery.image.ImageRepository;
import com.example.Mind_in_Canvas.dto.gallery.DrawingResponse;
import com.example.Mind_in_Canvas.dto.gallery.GalleryResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GalleryService {

    private final ImageRepository imageRepository;
    private final DrawingRepository drawingRepository;

    public GalleryService(ImageRepository imageRepository, DrawingRepository drawingRepository) {
        this.imageRepository = imageRepository;
        this.drawingRepository = drawingRepository;
    }

    public GalleryResponse getImage(UUID imageId) {

        String backgroundImageUrl = imageRepository.findImageUrlByImageId(imageId);
        List<DrawingResponse> drawings = drawingRepository.findAllByImageImageId(imageId)
                .stream()
                .map(drawing -> DrawingResponse.builder()
                        .positionX(drawing.getPositionX())
                        .positionY(drawing.getPositionY())
                        .originalImageUrl(drawing.getOriginalImage())
                        .build())
                .collect(Collectors.toList());

        return GalleryResponse.builder()
                .backgroundImageUrl(backgroundImageUrl)
                .drawings(drawings)
                .build();
    }
}
