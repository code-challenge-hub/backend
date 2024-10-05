package com.cch.codechallengehub.security.annotation;

import com.cch.codechallengehub.security.factory.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    String email() default "test@test.com";

    String password() default "qwe1234!";

    String role() default "USER";

    String nickname() default "TEST";
}
