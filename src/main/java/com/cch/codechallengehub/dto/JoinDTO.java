package com.cch.codechallengehub.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinDTO {
    private String nickname;
    private String email;
    private String password;
    private String role;
}
