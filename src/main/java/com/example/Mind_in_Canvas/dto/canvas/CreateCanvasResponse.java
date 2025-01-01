package com.example.Mind_in_Canvas.dto.canvas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCanvasResponse {

    private String status;
    private String redirect_url;
    private String initial_audio;
    private String initial_text;
}