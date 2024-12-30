package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import com.example.Mind_in_Canvas.domain.user.kid.KidRepository;
import com.example.Mind_in_Canvas.dto.canvas.AnalyzeDrawingRequest;
import com.example.Mind_in_Canvas.dto.canvas.AnalyzeDrawingResponse;
import com.example.Mind_in_Canvas.dto.canvas.CreateCanvasResponse;
import com.example.Mind_in_Canvas.dto.canvas.ContinueDrawingResponse;
import com.example.Mind_in_Canvas.shared.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class CanvasService {

    private final CanvasRepository canvasRepository;
    private final KidRepository kidRepository;

    @Autowired
    private final RestTemplate restTemplate;

    @Autowired
    public CanvasService(CanvasRepository canvasRepository, KidRepository kidRepository, RestTemplate restTemplate) {
        this.canvasRepository = canvasRepository;
        this.kidRepository = kidRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public CreateCanvasResponse createCanvas(UUID kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new ResourceNotFoundException("Kid not found"));

        Canvas canvas = Canvas.createCanvas(kid);
        canvasRepository.save(canvas);

        return CreateCanvasResponse.from(canvas);
    }

    public ContinueDrawingResponse continueCanvas(UUID canvasId) {
        String imageUrl = canvasRepository.findImageUrlByCanvasId(canvasId);
        return new ContinueDrawingResponse(imageUrl);
    }

    public AnalyzeDrawingResponse analyzeDrawing(AnalyzeDrawingRequest request) {
        // AI 서버와 통신하여 분석 결과를 받아오는 로직 추가
        String aiServerUrl = "AI_SERVER_URL/analyze"; // 실제 URL로 변경 필요
        return restTemplate.postForObject(aiServerUrl, request, AnalyzeDrawingResponse.class);
    }
}
