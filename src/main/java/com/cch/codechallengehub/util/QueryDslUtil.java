package com.cch.codechallengehub.util;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.PathBuilder;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QueryDslUtil {

	// 상위 클래스 필드 찾기
	public static Field findFieldHierarchy(Class<?> type, String property) throws NoSuchFieldException {
		Class<?> current = type;
		while (current != null) {
			try {
				return current.getDeclaredField(property);
			} catch (NoSuchFieldException e) {
				current = current.getSuperclass();
			}
		}
		throw new NoSuchFieldException("No Such Field " + property);
	}

	@SuppressWarnings("unchecked")
	public static Expression<? extends Comparable<?>> getPropertyPath(PathBuilder<?> entityPath,
		Class<?> type, String property) {
		if (type.equals(String.class)) {
			return entityPath.getString(property);
		} else if (type.equals(Integer.class)) {
			return entityPath.getNumber(property, Integer.class);
		} else if (type.equals(Long.class)) {
			return entityPath.getNumber(property, Long.class);
		} else if (type.equals(Double.class)) {
			return entityPath.getNumber(property, Double.class);
		} else if (type.equals(Float.class)) {
			return entityPath.getNumber(property, Float.class);
		} else if (type.equals(BigDecimal.class)) {
			return entityPath.getNumber(property, BigDecimal.class);
		} else if (type.equals(Date.class)) {
			return entityPath.getDate(property, Date.class);
		} else if (type.equals(LocalDate.class)) {
			return entityPath.getDate(property, LocalDate.class);
		} else if (type.equals(LocalDateTime.class)) {
			return entityPath.getDateTime(property, LocalDateTime.class);
		} else if (Comparable.class.isAssignableFrom(type)) {
			return entityPath.getComparable(property, (Class<? extends Comparable<?>>) type);
		}
		throw new IllegalArgumentException("Unsupported property type " + type.getName());
	}

}
