package com.cch.codechallengehub.domain;

import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "profile")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Profile extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @JoinColumn(name = "user_id")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "introduction")
    @Lob
    private String introduction;

    @Column(name = "job")
    private String job;

    @Column(name = "career")
    private Integer career;

    @Builder
    public Profile(User user, String introduction, String job, Integer career) {
        this.user = user;
        this.introduction = introduction;
        this.job = job;
        this.career = career;
    }

    public void updateProfile(String introduction, String job, Integer career) {
        this.introduction = introduction;
        this.job = job;
        this.career = career;
    }

}
