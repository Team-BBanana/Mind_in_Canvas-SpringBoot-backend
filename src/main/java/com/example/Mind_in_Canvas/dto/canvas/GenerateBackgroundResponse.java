package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class GenerateBackgroundResponse {

    private String imageAnalysis;
    private String summary;
    private String contents;
    private String backgroundImage;
    private String drawingName;
    
}
