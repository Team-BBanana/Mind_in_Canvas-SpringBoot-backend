package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class AnalyzeDrawingRequest {
    private String base64Img;
}
