package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CreateCanvasResponse {

    private UUID canvasId;
    private String status;
    private LocalDateTime createdAt;

}