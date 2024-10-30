package com.cch.codechallengehub.web.controller;

import com.cch.codechallengehub.dto.PasswordDto;
import com.cch.codechallengehub.dto.ProfileDto;
import com.cch.codechallengehub.security.CommonSecurityTest;
import com.cch.codechallengehub.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(controllers  = UserController.class)
class UserControllerTest extends CommonSecurityTest {

    @Value("${apiPrefix}") private String BASE_URL;
    @Autowired private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean private UserService userService;

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - getProfile API 성공")
    void getProfile_authenticatedUser_success() throws Exception {
        // given
        ProfileDto profileDto = ProfileDto.builder()
                                .introduction("test")
                                .job("BE")
                                .career(1)
                                .build();
        when(userService.getProfile(anyString())).thenReturn(profileDto);
        String requestJson = objectMapper.writeValueAsString(profileDto);

        // when & then
        mockMvc.perform(get(BASE_URL +"/v1/user/profile"))
                .andExpect(status().isOk())
                .andExpect(content().string(requestJson))
                .andDo(print());
        verify(userService, times(1)).getProfile(anyString());
    }

    @Test
    @DisplayName("인증되지않은사용자 - getProfile API 실패")
    void getProfile_noAuthenticatedUser_fail() throws Exception {
        // when & then
        mockMvc.perform(get(BASE_URL +"/v1/user/profile")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - setProfile API 성공")
    void setProfile_authenticatedUser_success() throws Exception {
        // given
        doNothing().when(userService).setProfile(anyString(), Mockito.any(ProfileDto.class));
        ProfileDto profileDto = ProfileDto.builder().job("test").build();
        String requestJson = objectMapper.writeValueAsString(profileDto);

        // when & then
        mockMvc.perform(post(BASE_URL + "/v1/user/profile")
                        .content(requestJson)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService, times(1)).setProfile(anyString(), Mockito.any(ProfileDto.class));
    }

    @Test
    @DisplayName("인증되지않은사용자 - setProfile API 실패")
    void setProfile_noAuthenticatedUser_fail() throws Exception {
        // given
        ProfileDto profileDto = new ProfileDto();

        // when & then
        mockMvc.perform(post(BASE_URL +"/v1/user/profile")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - changePassword API 성공")
    void changePassword_authenticatedUser_success() throws Exception {
        // given
        doNothing().when(userService).changePassword(anyString(), Mockito.any(PasswordDto.class));
        PasswordDto passwordDto = PasswordDto.builder().password("qwe123!").build();
        String requestJson = objectMapper.writeValueAsString(passwordDto);

        // when & then
        mockMvc.perform(patch(BASE_URL +"/v1/user/password")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        verify(userService, times(1)).changePassword(anyString(), Mockito.any(PasswordDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("인증된사용자 - changePassword API param validation 실패")
    void changePassword_authenticatedUser_fail() throws Exception {
        // given
        doNothing().when(userService).changePassword(anyString(), Mockito.any(PasswordDto.class));
        PasswordDto passwordDto = PasswordDto.builder().password("1234").build();
        String requestJson = objectMapper.writeValueAsString(passwordDto);

        // when & then
        mockMvc.perform(patch(BASE_URL +"/v1/user/password")
                        .contentType(APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증되지않은사용자 - changePassword API 실패")
    void changePassword_noAuthenticatedUser_fail() throws Exception {
        // when & then
        mockMvc.perform(patch(BASE_URL +"/v1/user/password")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }
}