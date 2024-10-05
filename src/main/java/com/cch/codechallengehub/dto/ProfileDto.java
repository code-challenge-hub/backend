package com.cch.codechallengehub.dto;

import com.cch.codechallengehub.domain.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private String introduction;
    private String job;
    private Integer career;

    public Profile toEntity() {
        return Profile.builder()
                .introduction(introduction)
                .job(job)
                .career(career)
                .build();
    }

    public static ProfileDto toDto(Profile profile) {

        return ProfileDto.builder()
                        .introduction(profile.getIntroduction())
                        .job(profile.getJob())
                        .career(profile.getCareer())
                        .build();
    }
}
