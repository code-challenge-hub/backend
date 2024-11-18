package com.cch.codechallengehub.repository;

import static com.cch.codechallengehub.domain.QChallenge.challenge;
import static com.cch.codechallengehub.domain.QChallengeTechStack.challengeTechStack;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.dto.ChallengeSearchCondition;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.cch.codechallengehub.util.QueryDslUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChallengeQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public Slice<ChallengeSearchResult> findSlice(ChallengeSearchCondition condition, Pageable pageable) {

		int pageSize = pageable.getPageSize();
		long offset = pageable.getOffset();
		Sort sort = pageable.getSort();
		OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(sort)
				.toArray(OrderSpecifier[]::new);

		List<ChallengeSearchResult> contents = jpaQueryFactory.select(challenge)
			.from(challenge)
			.join(challenge.challengeTechStacks, challengeTechStack)
			.where(challengeNameLike(condition.getChallengeNameLike())
				, techStacksIn(condition.getTechStacks())
				, levelEquals(condition.getLevel())
				, statusEquals(condition.getStatus()))
			.offset(offset)
			.limit(pageSize + 1)
			.orderBy()
			.distinct()
			.orderBy(orderSpecifiers)
			.fetch()
			.stream().map(ChallengeSearchResult::from)
			.toList();

		boolean hasNext = contents.size() > pageSize;
		if (hasNext) {
			contents = contents.subList(0, pageSize);
		}

		return new SliceImpl<>(contents, pageable, hasNext);
	}

	private BooleanExpression challengeNameLike(String challengeName) {
		return hasText(challengeName) ? challenge.challengeName.contains(challengeName) : null;
	}

	private BooleanExpression techStacksIn(List<String> techStacks) {
		return isEmpty(techStacks) ? null : challengeTechStack.stackName.in(techStacks);
	}

	private BooleanExpression statusEquals(ChallengeStatus status) {
		return status == null ? null : challenge.status.eq(status);
	}
	private BooleanExpression levelEquals(ChallengeLevel level) {
		return level == null ? null : challenge.level.eq(level);
	}

	private List<OrderSpecifier<?>> getOrderSpecifiers(Sort sort) {
		List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
		PathBuilder<Challenge> entityPath = new PathBuilder<>(Challenge.class, "challenge");
		sort.stream()
			.forEach(order -> {
				String property = order.getProperty();
				Expression<? extends Comparable<?>> path = getPropertyPath(entityPath, property);
				if (path != null) {
					orderSpecifiers.add(
						new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, path));
				}
			});
		return orderSpecifiers;
	}

	private Expression<? extends Comparable<?>> getPropertyPath(PathBuilder<?> entityPath, String property) {
		try {
			Field field = QueryDslUtil.findFieldHierarchy(Challenge.class, property);
			Class<?> type = field.getType();
			return QueryDslUtil.getPropertyPath(entityPath, type, property);
		} catch (NoSuchFieldException | IllegalArgumentException e) {
			log.error(e.getMessage());
		}
		return null;
	}
}
