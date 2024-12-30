package com.example.Mind_in_Canvas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    // 특정 경로의 API들만 문서화하기 위한 설정
    @Bean
    public GroupedOpenApi userControllerApi() {
        return GroupedOpenApi.builder()
                .group("User Controller API")  // 그룹 이름을 설정합니다.
                .pathsToMatch("/users/**")  // 문서화할 경로를 설정합니다.
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Mind in Canvas API").version("v1"))
                .paths(new Paths()
                        .addPathItem("/auth/login", createLoginPathItem())
                        .addPathItem("/oauth2/authorization/google", createGoogleOAuth2PathItem())
                        .addPathItem("/auth/logout", createLogoutPathItem()));
    }

    private PathItem createLoginPathItem() {
        return new PathItem()
                .post(new Operation()
                        .tags(List.of("auth"))
                        .summary("폼 로그인")
                        .description("폼 기반 로그인 인증을 처리합니다.")
                        .requestBody(new RequestBody()
                                .content(new Content().addMediaType("application/json",
                                        new MediaType().schema(new Schema<>()
                                                .type("object")
                                                .addProperties("email", new Schema<>().type("string").description("User Email"))
                                                .addProperties("password", new Schema<>().type("string").description("User Password"))
                                        )
                                ))
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse().description("Authentication success")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new Schema<>()
                                                        .type("object")
                                                        .addProperties("message", new Schema<>().type("string").description("Credential Authentication Success"))
                                                        .addProperties("user", new Schema<>().type("string").description("User Email"))
                                                )
                                        ))
                                )
                                .addApiResponse("401", new ApiResponse().description("Authentication Failed")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new Schema<>()
                                                        .type("object")
                                                        .addProperties("errormessage", new Schema<>().type("string").description("Credential Authentication Failed"))
                                                        .addProperties("User", new Schema<>().type("string").description("User Email"))
                                                )
                                        ))
                                )
                        )
                );

    }

    private PathItem createGoogleOAuth2PathItem() {
        return new PathItem()
                .get(new Operation()
                        .tags(List.of("auth"))
                        .summary("구글 OAuth2 로그인")
                        .description("구글 소셜 로그인을 위한 OAuth2 인증 엔드포인트로 리디렉션합니다.")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse().description("Authentication Success")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new Schema<>()
                                                        .type("object")
                                                        .addProperties("message", new Schema<>().type("string").description("Social Authentication Success"))
                                                        .addProperties("user", new Schema<>().type("string").description("User Email"))
                                                )
                                        ))
                                )
                                .addApiResponse("401", new ApiResponse().description("Authentication Failed")
                                        .content(new Content().addMediaType("application/json",
                                                new MediaType().schema(new Schema<>()
                                                        .type("object")
                                                        .addProperties("message", new Schema<>().type("string").description("사용자를 찾을 수 없습니다"))
                                                        .addProperties("user", new Schema<>().type("string").description("요청된 사용자 이름"))
                                                )
                                        ))
                                )

                        )
                );
    }

    private PathItem createLogoutPathItem() {
        return new PathItem()
                .post(new Operation()
                        .tags(List.of("auth"))
                        .summary("로그아웃")
                        .description("로그아웃을 수행하고 사용자 세션을 무효화합니다.")
                        .responses(new ApiResponses()
                                .addApiResponse("302", new ApiResponse().description("리다이렉트"))));
    }
}
