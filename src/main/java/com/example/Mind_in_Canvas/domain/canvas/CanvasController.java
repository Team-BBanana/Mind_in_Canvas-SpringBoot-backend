package com.example.Mind_in_Canvas.domain.canvas;

import com.example.Mind_in_Canvas.dto.canvas.*;
import com.example.Mind_in_Canvas.shared.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<Void> analyzeDrawing(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID canvasId,
            @RequestBody String base64image
    ) {
        if (!StringUtils.hasText(base64image)) {
            throw new ResourceNotFoundException("이미지 데이터가 없습니다.");
        }

        canvasService.analyzeDrawingRequest(canvasId, base64image);
        return ResponseEntity.ok().build();
    }


    // 배경에 이어그리기
    @GetMapping("/makefriend/{canvasId}")
    public ResponseEntity<MakeFriendResponse> makeFriend(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID canvasId
    ) {
        try {
            MakeFriendResponse response = canvasService.makeFriend(canvasId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("캔버스 불러오기 실패: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PostMapping("/done")
    public ResponseEntity<GenerateBackgroundResponse> doneCanvas(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID canvasId,
            @RequestBody(required = true) String originalDrawing
    ) {
        return ResponseEntity.ok(canvasService.makeBackgroundRequest(canvasId, originalDrawing));
    }

}