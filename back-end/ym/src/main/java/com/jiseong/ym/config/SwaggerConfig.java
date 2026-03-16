package com.jiseong.ym.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(springdoc-openapi) 설정
 * Swagger UI: http://localhost:8080/swagger-ui/index.html
 *
 * JWT 인증 사용 방법:
 *   1. POST /api/auth/login 으로 토큰 발급
 *   2. Swagger UI 우측 상단 "Authorize" 버튼 클릭
 *   3. 입력창에 발급받은 토큰 붙여넣기 → 이후 인증 필요 API 자동으로 토큰 포함
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // HTTP Bearer 방식의 JWT 인증 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 모든 API 엔드포인트에 JWT 인증 자물쇠 아이콘 표시
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth");

        return new OpenAPI()
                .info(new Info()
                        .title("YM - 대학생 팀원 모집 플랫폼 API")
                        .description("공모전, 졸업프로젝트, 스터디, 해커톤 등을 위한 팀원 모집 서비스")
                        .version("v1.0.0"))
                .addSecurityItem(securityRequirement)
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", securityScheme));
    }
}