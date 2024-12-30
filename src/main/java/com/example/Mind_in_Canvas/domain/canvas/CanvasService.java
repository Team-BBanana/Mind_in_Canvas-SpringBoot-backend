package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import com.example.Mind_in_Canvas.domain.user.kid.KidRepository;
import com.example.Mind_in_Canvas.domain.user.parent.User;
import com.example.Mind_in_Canvas.dto.canvas.*;
import com.example.Mind_in_Canvas.shared.exceptions.ExternalServerException;
import com.example.Mind_in_Canvas.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class CanvasService {

    private final CanvasRepository canvasRepository;
    private final KidRepository kidRepository;

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    public CanvasService(CanvasRepository canvasRepository, KidRepository kidRepository) {
        this.canvasRepository = canvasRepository;
        this.kidRepository = kidRepository;
    }

    // 새 캔버스에 그림 그리기
    @Transactional
    public CreateCanvasResponse createCanvas(UUID kidId) {
        Kid kid = kidRepository.findById(kidId)
                .orElseThrow(() -> new ResourceNotFoundException("Kid not found"));
        User parent = kidRepository.findParentByKidId(kidId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("robot_id", parent.getRobotId());
        requestBody.put("name", kid.getName());
        requestBody.put("age", kid.getAge());

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            WebClient webClient = WebClient.create(aiServerUrl);
            CreateCanvasResponse response = webClient.post()
                .uri("/drawing/new")
                .headers(httpHeaders -> httpHeaders.addAll(requestEntity.getHeaders()))
                .bodyValue(Objects.requireNonNull(requestEntity.getBody()))
                .retrieve()
                .bodyToMono(CreateCanvasResponse.class)
                .block();

            // 응답 상태 확인
            if (response.getStatus().equals("error")) {
                throw new ExternalServerException("로봇 호출 실패");
            }

            Canvas canvas = Canvas.createCanvas(kid);
            
            canvasRepository.save(canvas);
            return response;

        } catch (Exception e) {
            log.error("AI 서버 통신 중 오류 발생", e);
            throw new ExternalServerException("AI 서버와 통신 중 오류가 발생했습니다.");
        }
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
