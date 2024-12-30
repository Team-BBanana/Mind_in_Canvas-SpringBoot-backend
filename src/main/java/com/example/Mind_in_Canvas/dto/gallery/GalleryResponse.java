package com.example.Mind_in_Canvas.dto.gallery;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
import java.util.List;

@Getter
@Builder
public class GalleryResponse {

    private UUID imageId;
    private String backgroundImageUrl;
    private List<DrawingResponse> drawings;

}
