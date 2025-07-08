package com.devdo.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration // bean 관리
public class SwaggerConfig {

    private final String AUTH_TOKEN_HEADER = "Authorization";

    private Info apiInfo() {
        return new Info()
                .title("DevDo Swagger API")
                .description("DevDo Swagger API입니다.")
                .version("2.0");
    }

    @Bean
    ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .openapi("3.0.0")
                .info(apiInfo()) // API 정보 설정
                .addSecurityItem(new SecurityRequirement().addList(AUTH_TOKEN_HEADER)) // 보안 요구 사항 추가
                .components(new Components()
                        .addSecuritySchemes(AUTH_TOKEN_HEADER, new SecurityScheme() // 보안 스키마 추가
                                .name(AUTH_TOKEN_HEADER) // 보안 스키마 이름
                                .type(SecurityScheme.Type.HTTP) // 스키마 타입
                                .scheme("Bearer") // 인증 스키마
                                .bearerFormat("JWT"))); // Bearer 포맷

    }
}