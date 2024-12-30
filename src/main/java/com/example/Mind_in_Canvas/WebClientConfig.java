package com.example.Mind_in_Canvas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${AI_SERVER_URL}")
    private String aiServerUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(aiServerUrl)
                .build();
    }
}