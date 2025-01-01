package com.example.Mind_in_Canvas.domain.canvas;
import com.example.Mind_in_Canvas.domain.user.kid.Kid;
import com.example.Mind_in_Canvas.domain.user.kid.KidRepository;
import com.example.Mind_in_Canvas.dto.canvas.*;
import com.example.Mind_in_Canvas.shared.exceptions.ExternalServerException;
import com.example.Mind_in_Canvas.shared.exceptions.ResourceNotFoundException;
import com.example.Mind_in_Canvas.shared.utils.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CanvasService {

    private final CanvasRepository canvasRepository;
    private final KidRepository kidRepository;

    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    private final WebClient webClient = WebClient.builder()
        .exchangeStrategies(ExchangeStrategies.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024)) // 10MB로 설정
            .build())
        .build();

    // 새 캔버스에 그림 그리기
    @Transactional
    public CreateCanvasResponse createCanvas(String token, String title) {
        String kidId = jwtTokenProvider.getUsername(token);
        
        Kid kid = kidRepository.findById(UUID.fromString(kidId))
                .orElseThrow(() -> new ResourceNotFoundException("Kid not found"));

        // Create a new Canvas entity
        Canvas canvas = Canvas.builder()
                .kid(kid)
                .title(title)
                .build();
        canvasRepository.save(canvas);

        System.out.println("canvas: " + canvas.getCanvasId());

        // Prepare the request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("robot_id", "1234");
        requestBody.put("canvas_id", canvas.getCanvasId().toString());
        requestBody.put("name", kid.getName());
        requestBody.put("age", kid.getAge());

        // Send the request to the external service
        try {
            CreateCanvasResponse response = webClient.post()
                .uri("http://localhost:8081/drawing/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(CreateCanvasResponse.class)
                .block();

            if (response.getStatus().equals("error")) {
                throw new ExternalServerException("Failed to call external service");
            }

            return response;

        } catch (Exception e) {
            log.error("Error communicating with external service", e);
            throw new ExternalServerException("Error communicating with external service");
        }
    }

}
