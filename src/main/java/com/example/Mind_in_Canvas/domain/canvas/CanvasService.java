package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.domain.gallery.drawing.DrawingRepository;
import com.example.Mind_in_Canvas.domain.gallery.image.Image;
import com.example.Mind_in_Canvas.domain.gallery.image.ImageRepository;
import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import com.example.Mind_in_Canvas.domain.user.kid.KidRepository;
import com.example.Mind_in_Canvas.domain.user.parent.User;
import com.example.Mind_in_Canvas.dto.canvas.*;
import com.example.Mind_in_Canvas.dto.gallery.DrawingResponse;
import com.example.Mind_in_Canvas.shared.exceptions.ExternalServerException;
import com.example.Mind_in_Canvas.shared.exceptions.InternalServerException;
import com.example.Mind_in_Canvas.shared.exceptions.ResourceNotFoundException;
import com.example.Mind_in_Canvas.webSocket.DrawingWebSocketHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CanvasService {

    private final CanvasRepository canvasRepository;
    private final KidRepository kidRepository;
    private final WebClient webClient;
    private final DrawingRepository drawingRepository;
    private final ImageRepository imageRepository;
    private final DrawingWebSocketHandler drawingWebSocketHandler;

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

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

    public MakeFriendResponse makeFriend(UUID canvasId) {
        Image backgroundImage = canvasRepository.findImageByCanvasId(canvasId);

        List<DrawingResponse> drawings = drawingRepository.findAllByImageImageId(backgroundImage.getImageId())
                .stream()
                .map(drawing -> DrawingResponse.builder()
                        .positionX(drawing.getPositionX())
                        .positionY(drawing.getPositionY())
                        .originalImageUrl(drawing.getOriginalImage())
                        .build())
                .toList();

        return MakeFriendResponse.builder()
                .canvasId(canvasId)
                .backgroundImageUrl(backgroundImage.getImageUrl())
                .drawings(drawings)
                .build();
    }

    public void analyzeDrawingRequest(UUID canvasId, String base64Img) {
        AnalyzeDrawingRequest message = AnalyzeDrawingRequest.builder()
                .canvasId(canvasId)
                .base64Img(base64Img)
                .build();

        drawingWebSocketHandler.sendMessage(message);
    }

    public GenerateBackgroundResponse makeBackgroundRequest(UUID canvasId, String originalDrawing) {
        try {
            // AI 서버에 배경 생성 요청 보내기
            GenerateBackgroundResponse aiResponse = sendGenerateBackgroundRequest(originalDrawing)
                    .block();
            Image image = Image.createImage(aiResponse.getBackgroundImage(), aiResponse.getImageAnalysis());
            imageRepository.save(image);

            return GenerateBackgroundResponse.builder()
                    .imageAnalysis(aiResponse.getImageAnalysis())
                    .summary(aiResponse.getSummary())
                    .contents(aiResponse.getContents())
                    .backgroundImage(aiResponse.getBackgroundImage())
                    .drawingName(aiResponse.getDrawingName())
                    .build();

        } catch (Exception e) {
            log.error("AI 배경 생성 또는 저장 실패: ", e);
            throw new RuntimeException("AI 배경 생성 처리 중 오류가 발생했습니다.");
        }
    }

    // AI 배경 생성 요청
    private Mono<GenerateBackgroundResponse> sendGenerateBackgroundRequest(String originalDrawing) {
        return webClient.post()
                .uri("/generate-background")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(originalDrawing)
                .retrieve()
                .bodyToMono(GenerateBackgroundResponse.class)
                .onErrorMap(error -> {
                    if (error instanceof WebClientResponseException) {
                        log.error("AI 서버 통신 오류: {}", error.getMessage(), error);
                        return new ExternalServerException("AI 서버와 통신 중 오류가 발생했습니다.");
                    }
                    log.error("예상치 못한 오류: {}", error.getMessage(), error);
                    return new InternalServerException("서비스 처리 중 오류가 발생했습니다.");
                });
    }

}
