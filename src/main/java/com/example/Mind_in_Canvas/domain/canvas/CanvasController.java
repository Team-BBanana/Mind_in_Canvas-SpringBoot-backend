package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.dto.canvas.*;
import com.example.Mind_in_Canvas.shared.ApiResponse;
import com.example.Mind_in_Canvas.shared.exceptions.ExternalServerException;
import com.example.Mind_in_Canvas.shared.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

@RestController
@RequestMapping("/api/canvas")
@Slf4j
public class CanvasController {


    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    private final CanvasService canvasService;

    public CanvasController(CanvasService canvasService) {
        this.canvasService = canvasService;
    }

    //프론트 에서 캔버스 생성 요청 받아서 캔버스 생성
    @PostMapping("/new/{kidId}")
    public ResponseEntity<CreateCanvasResponse> createCanvas(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID kidId
    ) {
        log.info("Create new canvas request for kidId: {}", kidId);
        try {
            CreateCanvasResponse response = canvasService.createCanvas(kidId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to create canvas: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // 일정시간 입력 없을 시 AI 그림 분석 요청 (비동기처리)
    @PostMapping("/analyze/{canvasId}")
    public Mono<ResponseEntity<AnalyzeDrawingResponse>> analyzeDrawing(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID canvasId,
            @RequestBody AnalyzeDrawingRequest request
    ) throws BadRequestException {
        if (!StringUtils.hasText(request.getBase64Img())) {
            throw new BadRequestException("이미지 데이터가 없습니다.");
        }

        return canvasService.sendToAiServer(createRequestBody(request))
                .map(responseEntity -> ResponseEntity.ok(responseEntity.getBody()))
                .onErrorResume(e -> {
                    log.error("AI 서버 통신 오류: {}", e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    private AnalyzeDrawingRequest createRequestBody(AnalyzeDrawingRequest request) {
        AnalyzeDrawingRequest requestBody = new AnalyzeDrawingRequest();
        requestBody.setBase64Img(request.getBase64Img());
        return requestBody;
    }

    @GetMapping("/{canvasId}")
    public ApiResponse<ContinueDrawingResponse> continueCanvas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID imageId
    ) {
        try {
            // 캔버스 정보를 가져오는 서비스 호출
            ContinueDrawingResponse response = canvasService.continueCanvas(imageId);
            return ApiResponse.success(response);
        } catch (Exception e) {
            log.error("캔버스 불러오기 실패: ", e);
            return ApiResponse.error("CANVAS_CONTINUE_FAILED", e.getMessage());
        }
    }

    @PostMapping("/done")
    public ApiResponse<CompleteDrawingResponse> doneCanvas(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam UUID canvasId,
            @RequestBody(required = true) String original_drawing
    ) {
        // HTTP 요청 엔티티 생성
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("original_drawing", original_drawing);
        requestBody.put("canvasId", canvasId.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            // ai 서버로 요청 보내기
            ResponseEntity<CompleteDrawingResponse> response = restTemplate.exchange(
                    aiServerUrl + "/generate-background", // 파이썬 서버 엔드포인트
                    HttpMethod.POST,
                    requestEntity,
                    CompleteDrawingResponse.class
            );
            return ApiResponse.success(response.getBody());
        } catch (HttpClientErrorException e) {
            log.error("AI 서버 통신 오류", e);
            throw new ExternalServerException("AI 서버와 통신 중 오류가 발생했습니다.");
        } catch (Exception e) {
            log.error("예상치 못한 오류", e);
            throw new InternalServerException("서비스 처리 중 오류가 발생했습니다.");
        }
    }

}