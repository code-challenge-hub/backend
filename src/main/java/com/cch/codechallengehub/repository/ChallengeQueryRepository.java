package com.cch.codechallengehub.repository;

import static com.cch.codechallengehub.domain.QChallenge.challenge;
import static com.cch.codechallengehub.domain.QChallengeTechStack.challengeTechStack;
import static com.cch.codechallengehub.domain.QTeam.team;
import static com.cch.codechallengehub.domain.QTechStack.techStack;
import static org.springframework.util.StringUtils.hasText;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.Team;
import com.cch.codechallengehub.dto.ChallengeSearchCondition;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.cch.codechallengehub.dto.ChallengeSearchTechStackResult;
import com.cch.codechallengehub.util.QueryDslUtil;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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

    public Slice<ChallengeSearchResult> findSlice(ChallengeSearchCondition condition,
        Pageable pageable) {

        int pageSize = pageable.getPageSize();
        long offset = pageable.getOffset();
        Sort sort = pageable.getSort();
        OrderSpecifier<?>[] orderSpecifiers = getOrderSpecifiers(sort)
            .toArray(OrderSpecifier[]::new);

        List<Challenge> challenges = findChallenges(condition, offset, pageSize, orderSpecifiers);
        List<Long> idList = challenges.stream()
            .map(Challenge::getId)
            .toList();

        Map<Long, List<Team>> teamsMap = findTeamsToMap(idList);
        Map<Long, List<ChallengeSearchTechStackResult>> techStacksMap = findTechStacksToMap(idList);

        List<ChallengeSearchResult> contents = challenges.stream()
            .map(challenge -> {
                List<Team> teams = teamsMap.get(challenge.getId());
                int participantsNum = teams == null ? 0 : teams.size();

                List<ChallengeSearchTechStackResult> techStacks = techStacksMap.get(
                    challenge.getId());
                return ChallengeSearchResult.builder()
                    .challengeId(challenge.getId())
                    .challengeName(challenge.getChallengeName())
                    .level(challenge.getLevel())
                    .status(challenge.getStatus())
                    .challengeDesc(challenge.getChallengeDesc())
                    .techStacks(techStacks)
                    .createdDate(challenge.getCreatedDate())
                    .views(challenge.getViewCount())
                    .thumbnail(challenge.getThumbnail())
                    .liked(false)
                    .likeCount(0)
                    .participantsNum(participantsNum)
                    .recruitNum(challenge.getRecruit().getNumber())
                    .build();
            })
            .toList();

        boolean hasNext = contents.size() > pageSize;
        if (hasNext) {
            contents = contents.subList(0, pageSize);
        }

        return new SliceImpl<>(contents, pageable, hasNext);
    }

    private List<Challenge> findChallenges(ChallengeSearchCondition condition, long offset,
        int pageSize,
        OrderSpecifier<?>[] orderSpecifiers) {
        return jpaQueryFactory.select(challenge)
            .from(challenge)
            .join(challenge.challengeTechStacks, challengeTechStack)
            .where(challengeNameOrTechStackLike(condition.getSearchLike())
                , levelEquals(condition.getLevel())
                , statusEquals(condition.getStatus()))
            .offset(offset)
            .limit(pageSize + 1)
            .orderBy(orderSpecifiers)
            .fetch();
    }

    private Map<Long, List<Team>> findTeamsToMap(List<Long> idList) {
        return jpaQueryFactory
            .select(team)
            .from(team)
            .where(team.challenge.id.in(idList))
            .stream()
            .collect(Collectors.groupingBy(team -> team.getChallenge().getId()));
    }

    private Map<Long, List<ChallengeSearchTechStackResult>> findTechStacksToMap(
        List<Long> idList) {
        return jpaQueryFactory.select(
                Projections.constructor(
                    ChallengeSearchTechStackResult.class,
                    challengeTechStack.challenge.id,
                    challengeTechStack.stackName,
                    techStack.icon
                ))
            .from(challengeTechStack)
            .leftJoin(challengeTechStack.techStack, techStack)
            .where(challengeTechStack.challenge.id.in(idList))
            .stream()
            .collect(Collectors.groupingBy(ChallengeSearchTechStackResult::getChallengeId));
    }

    private BooleanExpression challengeNameOrTechStackLike(String searchLike) {
        return hasText(searchLike) ?
            challenge.challengeName.contains(searchLike)
                .or(challengeTechStack.stackName.contains(searchLike)) : null;
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
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, challenge.createdDate));
        }
        return orderSpecifiers;
    }

    private Expression<? extends Comparable<?>> getPropertyPath(PathBuilder<?> entityPath,
        String property) {
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
