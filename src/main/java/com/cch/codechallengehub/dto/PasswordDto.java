package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.domain.User;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{4,16}$",
            message = "Password must be 4-16 characters long and include at least one letter, one number, and one special character")
    private String password;

    public User toEntity() {
        return User.builder()
                .password(password)
                .build();
    }
}