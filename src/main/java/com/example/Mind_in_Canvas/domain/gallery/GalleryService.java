package com.example.Mind_in_Canvas.domain.gallery;

import com.example.Mind_in_Canvas.domain.gallery.drawing.Drawing;
import com.example.Mind_in_Canvas.domain.gallery.drawing.DrawingRepository;
import com.example.Mind_in_Canvas.domain.gallery.image.Image;
import com.example.Mind_in_Canvas.domain.gallery.image.ImageRepository;
import com.example.Mind_in_Canvas.dto.gallery.AiEnhancementResponse;
import com.example.Mind_in_Canvas.dto.gallery.DrawingResponse;
import com.example.Mind_in_Canvas.dto.gallery.GalleryResponse;
import org.springframework.http.ResponseEntity;
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

    // 갤러리에서 특정 이미지 조회 api
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

    // 갤러리에서 특정 이미지 삭제 api
    public ResponseEntity<String> hideImage(UUID imageId) {
        try {
            Image image = imageRepository.findImageByImageId(imageId);
            image.setIsDeleted(true);
            imageRepository.save(image);

            // 응답 JSON으로 잘 가는지 확인 필요
            String jsonResponse = new StringBuilder()
                    .append("{\"message\": \"delete success\"}")
                    .toString();
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            throw new RuntimeException("delete failed", e);
        }
    }

    // <멋있게 만들기> 버튼 누르면 프론트단에서 '어떤 친구를 멋있게 할까요?' 하고 선택하게 해야함
    public AiEnhancementResponse getEnhancedImage(UUID drawingId) {

        String originalImageUrl = drawingRepository.findOriginalImageByDrawingId(drawingId);
        Drawing drawing = drawingRepository.findByDrawingId(drawingId);
        String enhancedImageUrl;
        try {
            // AI 서버에 이미지 처리 요청  -- 구현 필요
            enhancedImageUrl = requestEnhancedImageFromAI(originalImageUrl);
        } catch (Exception e) {
            throw new RuntimeException("AI 이미지 생성 실패");
        }

        drawing.setFinalImage(enhancedImageUrl);
        drawingRepository.save(drawing);

        return AiEnhancementResponse.builder()
                .DrawingId(drawingId)
                .positionX(drawing.getPositionX())
                .positionY(drawing.getPositionY())
                .newImageUrl(enhancedImageUrl)
                .build();
    }

    private String requestEnhancedImageFromAI(String originalImageUrl) {
        return "추후 구현";
    }
}
