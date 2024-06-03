package com.cch.codechallengehub.springdoc.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(apiInfo())
                .servers(apiServers());
    }

    private Info apiInfo() {
        return new Info()
                .title("Code Challenge Hub API") // API의 제목
                .description("Code Challenge Hub application") // API에 대한 설명
                .version("v0.0.1"); // API의 버전
    }

    private List<Server> apiServers() {
        return List.of(new Server().url("http://localhost:8080").description("local"));
    }
}
