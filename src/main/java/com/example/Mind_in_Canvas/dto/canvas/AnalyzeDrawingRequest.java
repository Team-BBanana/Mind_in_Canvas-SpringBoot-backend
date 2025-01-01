package com.example.Mind_in_Canvas.dto.canvas;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class AnalyzeDrawingRequest {
    private String canvas_id;
    private String image_url;
    private String objectPositions; // New field for object positions


    public AnalyzeDrawingRequest(String canvasId, String imageUrl, String objectPositions) {
        this.canvas_id = canvasId;
        this.image_url = imageUrl;
        this.objectPositions = objectPositions;
    }
}
