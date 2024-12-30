package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data @Builder
public class CreateCanvasRequest {

    private UUID robotId;
    private String kidName;
    private int kidAge;
//    private UUID canvasId;

}
