package com.cch.codechallengehub.web.controller;


import com.cch.codechallengehub.dto.auth.AuthEmailVerificationDto;
import com.cch.codechallengehub.security.CommonSecurityTest;
import com.cch.codechallengehub.service.AuthEmailService;
import com.cch.codechallengehub.web.dto.auth.AuthCodeVerificationRequest;
import com.cch.codechallengehub.web.dto.auth.AuthEmailRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ActiveProfiles("test")
@WebMvcTest(controllers  = AuthEmailController.class)
class AuthEmailControllerTest extends CommonSecurityTest {

    @Value("${apiPrefix}") private String BASE_URL;
    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean private AuthEmailService authEmailService;

    @Test
    @DisplayName("emaile로 인증 코드 발송 API 성공")
    void sendVerificationCodeEmail_Success() throws Exception {

        //given
        String testEmail = "test@test.com";
        doNothing().when(authEmailService).sendVerificationCodeEmail(anyString());
        AuthEmailRequest request = AuthEmailRequest.builder().email(testEmail).build();

        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(post(BASE_URL +"/v1/auth/email/code")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(authEmailService, times(1)).sendVerificationCodeEmail(testEmail);
    }

    @Test
    @DisplayName("인증 코드 검증 API 성공")
    void verificationCode_Success() throws Exception {
        String testEmail = "test@test.com";
        String verificationCode = "123456";

        AuthCodeVerificationRequest request = AuthCodeVerificationRequest.builder()
                                            .code(verificationCode)
                                            .email(testEmail).build();
        AuthEmailVerificationDto dto = AuthEmailVerificationDto.builder()
                                            .email(testEmail)
                                            .verificationCode(verificationCode)
                                            .attemptCount(1)
                                            .isDone(true)
                                            .build();

        when(authEmailService.verificationCode(anyString(), anyString())).thenReturn(dto);

        String requestJson = objectMapper.writeValueAsString(request);
        String responseJson = objectMapper.writeValueAsString(dto);
        mockMvc.perform(post(BASE_URL +"/v1/auth/email/verification")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().string(responseJson));

        verify(authEmailService, times(1)).verificationCode(testEmail, verificationCode);
    }

    @Test
    @DisplayName("emaile로 인증 코드 발송 API 실패 - email not null")
    void sendVerificationCodeEmail_InvalidRequest() throws Exception {
        mockMvc.perform(post(BASE_URL +"/v1/auth/email/code")
                        .contentType(APPLICATION_JSON)
                        .content("{\"email\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증 코드 검증 API 실패 - email and code not null")
    void verificationCode_InvalidRequest() throws Exception {
        mockMvc.perform(post(BASE_URL +"/v1/auth/email/verification")
                        .contentType(APPLICATION_JSON)
                        .content("{\"email\":\"\", \"code\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}