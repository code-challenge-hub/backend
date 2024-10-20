package com.cch.codechallengehub.entity;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({AuditingConfig.class})
@Transactional
class AuditingEntityTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void input_created_date() {
        // given
        User testUser = User.builder()
                            .nickname("test1")
                            .email("gagga@gmail.com")
                            .password("123")
                            .build();
        // when
        em.persist(testUser);
        // then
        LocalDateTime createdDate = testUser.getCreatedDate();
        assertThat(createdDate).isNotNull();
    }

    @Test
    void input_modified_date() throws Exception {
        // given
        User testUser = User.builder()
                            .nickname("test1")
                            .email("gagga@gmail.com")
                            .password("123")
                            .build();
        // when
        em.persist(testUser);
        Thread.sleep(500);
        testUser.changePassword("456");
        em.flush();
        em.clear();

        // then
        User findUser = em.find(User.class, testUser.getId());
        LocalDateTime createdDate = findUser.getCreatedDate();
        LocalDateTime modifiedDate = findUser.getModifiedDate();

        assertThat(modifiedDate).isAfter(createdDate);
    }
}