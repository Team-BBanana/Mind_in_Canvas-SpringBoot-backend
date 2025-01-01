package com.example.Mind_in_Canvas.dto.canvas;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AnalyzeDrawingResponse {
  private String status;
  private String canvasId;
  private String imageUrl;
  private String analysis;
  private String summary;
  private List<String> conversationHistory;
  private String backgroundImage;
  private String drawingName;

  public AnalyzeDrawingResponse(String canvasId, String imageUrl, String status, String analysis, String summary, List<String> conversationHistory, String backgroundImage, String drawingName) {
    this.canvasId = canvasId;
    this.imageUrl = imageUrl;
    this.status = status;
    this.analysis = analysis;
    this.summary = summary;
    this.conversationHistory = conversationHistory;
    this.backgroundImage = backgroundImage;
    this.drawingName = drawingName;
  }
}
