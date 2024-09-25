package com.cch.codechallengehub.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {

	Class<? extends Enum<?>> enumClass();
	String message() default "value is not valid";

	// 특정 상황에서만 검증 할 수 있게 그룹 지정
	Class<?>[] groups() default {};

	// 검증 실패 시 전달할 메타 데이터를 지정
	Class<?extends Payload>[] payload() default {};


}
