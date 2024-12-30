package com.example.Mind_in_Canvas.config;

import com.example.Mind_in_Canvas.webSocket.DrawingWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(drawingWebSocketHandler(new ObjectMapper()), "/drawing/send")
                .setAllowedOrigins("*");
    }

    @Bean
    public DrawingWebSocketHandler drawingWebSocketHandler(ObjectMapper objectMapper) {
        return new DrawingWebSocketHandler(objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}