package com.cch.codechallengehub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${apiPrefix}")
    private String apiPrefix;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(apiInfo())
                .servers(apiServers())
                .paths(paths());
    }

    private Info apiInfo() {
        return new Info()
                .title("Code Challenge Hub API") // API의 제목
                .description("Code Challenge Hub application") // API에 대한 설명
                .version("v0.0.1") // API의 버전
                .contact(new Contact().name("Code Challenge Hub")
                        .email("haeunchoi.dev@gmail.com").url(apiPrefix));
    }

    private List<Server> apiServers() {
        return List.of(new Server().url("http://localhost:8080").description("local"));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private Paths paths() {
        return new Paths().addPathItem(apiPrefix + "/auth/login", login());
    }

    private PathItem login() {
        return new PathItem()
                .post(new io.swagger.v3.oas.models.Operation()
                        .addTagsItem("auth")
                        .summary("Login")
                        .description("User login")
                        .requestBody(new RequestBody()
                                .description("Login credentials")
                                .required(true)
                                .content(new Content()
                                        .addMediaType("application/x-www-form-urlencoded",
                                                new MediaType().schema(new Schema()
                                                        .type("object")
                                                        .addProperty("email", new Schema<>().type("string").example("test@gmail.com"))
                                                        .addProperty("password", new Schema<>().type("string").example("1234"))))))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Successful login")
                                        .addHeaderObject("Authorization", new Header()
                                                .description("JWT Token")
                                                .schema(new Schema<>().type("string"))))
                                .addApiResponse("401", new ApiResponse()
                                        .description("Unauthorized"))));
    }
}
