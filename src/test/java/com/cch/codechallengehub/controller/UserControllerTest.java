package com.cch.codechallengehub.controller;

import com.cch.codechallengehub.domain.User;
import com.cch.codechallengehub.dto.ProfileDto;
import com.cch.codechallengehub.repository.UserRepository;
import com.cch.codechallengehub.security.annotation.WithMockCustomUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository userRepository;

    private static final String BASE_URL = "/api/v1/user";

    @BeforeEach
    public void setUp() {
       User user = User.builder().email("test@test.com").build();
       userRepository.save(user);
    }

    @Test
    @WithMockCustomUser
    @DisplayName("Profile Save Test")
    void save_profile_test() throws Exception {

        //when
        String body = mapper.writeValueAsString(
                ProfileDto.builder().job("BE").career(0).introduction("TEST").build()
        );
        //then
        mvc.perform(post(BASE_URL + "/profile")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
}