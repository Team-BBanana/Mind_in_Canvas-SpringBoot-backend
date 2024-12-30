package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
@Data @Builder
public class AnalyzeDrawingRequest {
    private UUID canvasId;
    private String base64Img;
}
