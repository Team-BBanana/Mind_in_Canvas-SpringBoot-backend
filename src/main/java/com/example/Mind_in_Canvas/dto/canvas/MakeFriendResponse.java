package com.example.Mind_in_Canvas.dto.canvas;

import com.example.Mind_in_Canvas.dto.gallery.DrawingResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class MakeFriendResponse {

    private UUID canvasId;
    private String backgroundImageUrl;
    private List<DrawingResponse> drawings;
}
