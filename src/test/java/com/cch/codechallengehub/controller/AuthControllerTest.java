package com.cch.codechallengehub.controller;

import com.cch.codechallengehub.dto.UserDto;
import com.cch.codechallengehub.security.CommonSecurityTest;
import com.cch.codechallengehub.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(controllers  = AuthController.class)
public class AuthControllerTest  extends CommonSecurityTest {

    @Value("${apiPrefix}") private String BASE_URL;
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean private AuthService authService;

    @Test
    @DisplayName("joinProcess API 성공")
    void joinProcess_success() throws Exception {
        //given
        doNothing().when(authService).joinProcess(Mockito.any(UserDto.class));
        UserDto userDto = UserDto.builder()
                        .nickname("test")
                        .email("test@test.com")
                        .password("qwe123!")
                        .role("USER")
                        .build();
        String requestJson = objectMapper.writeValueAsString(userDto);

        // when & then
        mockMvc.perform(post(BASE_URL +"/auth/join")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                        .andExpect(status().isOk())
                        .andDo(print());

        verify(authService, times(1)).joinProcess(Mockito.any(UserDto.class));
    }

    @Test
    @DisplayName("joinProcess API param validation 실패")
    void joinProcess_fail() throws Exception {
        //given
        doNothing().when(authService).joinProcess(Mockito.any(UserDto.class));
        UserDto userDto = UserDto.builder()
                .nickname("test")
                .email("test@test.com")
                .password("1234")
                .role("USER")
                .build();
        String requestJson = objectMapper.writeValueAsString(userDto);

        // when & then
        mockMvc.perform(post(BASE_URL +"/auth/join")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("reissue API 성공")
    void reissue_success() throws Exception {
        doNothing().when(authService).reisuue(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
        // when & then
        mockMvc.perform(post(BASE_URL +"/auth/reissue")
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk());

        verify(authService, times(1)).reisuue(Mockito.any(HttpServletRequest.class), Mockito.any(HttpServletResponse.class));
    }
}
