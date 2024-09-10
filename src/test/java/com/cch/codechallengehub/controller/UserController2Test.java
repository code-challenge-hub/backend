package com.cch.codechallengehub.controller;

import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.ProfileDto;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.security.annotation.WithMockCustomUser;
import com.cch.codechallengehub.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserController.class, ObjectMapper.class})
@WebAppConfiguration
@AutoConfigureMockMvc
class UserController2Test {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String BASE_URL = "/api/v1/user";

    @Test
    @WithMockCustomUser
    @DisplayName("Profile Save Test")
    void save_profile_test() throws Exception {
        String userName = "test@test.com";
        ProfileDto profileDto = new ProfileDto();
        //when
        doNothing().when(userService).setProfile(userName, profileDto);

        //then
        mockMvc.perform(post(BASE_URL + "/profile"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}