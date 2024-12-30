package com.example.Mind_in_Canvas.dto.gallery;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AiEnhancementResponse {

    private UUID DrawingId;
    private float positionX;
    private float positionY;
    private String newImageUrl;
}
