package com.example.Mind_in_Canvas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .title("Mind in Canvas API Documentation")  // API 문서의 제목
                .version("v1.0.0")                         // API 버전
                .description("Mind in Canvas 프로젝트의 API 문서입니다")  // API 설명
                .license(new License()
                        .name("Apache License Version 2.0")  // 라이선스 정보
                        .url("http://www.apache.org/licenses/LICENSE-2.0")
                );

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        return new OpenAPI()
                .info(info)
                .components(new Components()
                        .addSecuritySchemes("bearer-key", securityScheme)
                );
    }
}