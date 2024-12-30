package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter @Data
public class GenerateBackgroundRequest {

    private String originalDrawing;
    private UUID canvasId;

}
