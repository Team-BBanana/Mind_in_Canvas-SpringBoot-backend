package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Getter;

@Getter
public class AnalyzeDrawingRequest {
    private String base64Img;

    public AnalyzeDrawingRequest(String base64Img) {
        this.base64Img = base64Img;
    }
}
