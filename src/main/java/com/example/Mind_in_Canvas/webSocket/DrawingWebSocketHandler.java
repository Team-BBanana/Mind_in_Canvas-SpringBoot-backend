package com.example.Mind_in_Canvas.webSocket;

import com.example.Mind_in_Canvas.dto.canvas.AnalyzeDrawingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class DrawingWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        log.info("웹소켓 연결 성공: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("페이로드 수신: {}", payload);

        // JSON 문자열을 AnalyzeDrawingRequest 객체로 변환
        AnalyzeDrawingRequest request = objectMapper.readValue(payload, AnalyzeDrawingRequest.class);
        // 여기서 필요한 처리 수행
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        log.info("웹소켓 연결 종료: {}", session.getId());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("웹소켓 에러 발생: {}", exception.getMessage());
    }

    public void sendMessage(AnalyzeDrawingRequest message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            TextMessage textMessage = new TextMessage(payload);

            // 모든 연결된 세션에 메시지 전송
            sessions.values().forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(textMessage);
                        log.debug("메시지 전송 성공: {}", payload);
                    }
                } catch (IOException e) {
                    log.error("메시지 전송 실패: {}", e.getMessage(), e);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패: {}", e.getMessage(), e);
        }
    }
}