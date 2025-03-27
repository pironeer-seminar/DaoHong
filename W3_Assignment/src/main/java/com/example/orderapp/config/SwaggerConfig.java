package com.example.orderapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("주문 관리 시스템 API")
                        .description("Spring Boot를 이용한 주문 관리 시스템 API 문서")
                        .version("v1.0"));
    }
} 