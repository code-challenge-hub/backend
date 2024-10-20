package com.cch.codechallengehub.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

	private Class<? extends Enum<?>> enumClass;

	@Override
	public void initialize(ValidEnum constraintAnnotation) {
		enumClass = constraintAnnotation.enumClass();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		Enum<?>[] enumConstants = enumClass.getEnumConstants();
		return Arrays.stream(enumConstants)
			.anyMatch(enumConstant -> enumConstant.name().equals(value));
	}
}
