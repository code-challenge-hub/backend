package com.cch.codechallengehub.config;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.cch.codechallengehub.web.dto.user.UserCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class SecurityCorsTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void devCorsSuccessTest() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
            .nickname("test")
            .email("test@test.com")
            .password("qwe123!")
            .role("USER")
            .build();
        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/auth/join")
                .content(body)
                .contentType(APPLICATION_JSON)
                .header("Origin", "http://localhost:5000"))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5000"))
            .andDo(print());
    }

    @Test
    void devCorsFailTest() throws Exception {
        UserCreateRequest request = UserCreateRequest.builder()
            .nickname("test")
            .email("test@test.com")
            .password("qwe123!")
            .role("USER")
            .build();
        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/auth/join")
                .content(body)
                .contentType(APPLICATION_JSON)
                .header("Origin", "http://test.com"))
            .andExpect(status().isForbidden())
            .andExpect(content().string("Invalid CORS request"))
            .andDo(print());
    }
}