package com.cch.codechallengehub.repository;

import static com.cch.codechallengehub.constants.ChallengeLevel.ADVANCED;
import static com.cch.codechallengehub.constants.ChallengeLevel.BEGINNER;
import static com.cch.codechallengehub.constants.RecruitType.FIRST_COME_FIRST_SERVE;
import static org.assertj.core.api.Assertions.assertThat;

import com.cch.codechallengehub.config.AuditingConfig;
import com.cch.codechallengehub.config.QueryDslConfig;
import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.domain.Challenge;
import com.cch.codechallengehub.domain.ChallengeTechStack;
import com.cch.codechallengehub.domain.Period;
import com.cch.codechallengehub.domain.Recruit;
import com.cch.codechallengehub.dto.ChallengeSearchCondition;
import com.cch.codechallengehub.dto.ChallengeSearchResult;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

@DataJpaTest
@Import({QueryDslConfig.class, AuditingConfig.class})
class ChallengeQueryRepositoryTest {

	@Autowired
	JPAQueryFactory jpaQueryFactory;

	@Autowired
	EntityManager em;

	ChallengeQueryRepository challengeQueryRepository;

	@BeforeEach
	void setUp() {
		challengeQueryRepository = new ChallengeQueryRepository(jpaQueryFactory);
	}

	@Test
	void search_challenge_get_slice() {
		// given
		for (int i = 0; i < 11; i++) {
			createChallenge(i);
		}
		ChallengeSearchCondition condition = ChallengeSearchCondition.builder()
			.build();
		PageRequest pageRequest = PageRequest.of(0, 10);
		// when
		Slice<ChallengeSearchResult> slice = challengeQueryRepository.findSlice(condition, pageRequest);
		// then
		List<ChallengeSearchResult> content = slice.getContent();
		int size = slice.getSize();
		boolean hasNext = slice.hasNext();
		Pageable pageable = slice.getPageable();
		int pageNumber = pageable.getPageNumber();
		int pageSize = pageable.getPageSize();
		long offset = pageable.getOffset();
		assertThat(content.size()).isEqualTo(size);
		assertThat(hasNext).isTrue();
		assertThat(pageNumber).isEqualTo(0);
		assertThat(pageSize).isEqualTo(10);
		assertThat(offset).isEqualTo(0);
	}

	private void createChallenge(int index) {
		LocalDateTime now = LocalDateTime.now();

		Recruit recruit = Recruit.builder()
			.number(5)
			.period(new Period(now, now.plusDays(1)))
			.type(FIRST_COME_FIRST_SERVE)
			.build();
		ChallengeTechStack spring = ChallengeTechStack.builder()
			.stackName("Spring " + index)
			.build();
		Challenge challenge = Challenge.builder()
			.challengeName("1-1")
			.level(BEGINNER)
			.period(new Period(now, now.plusDays(5)))
			.recruit(recruit)
			.funcRequirements(List.of("로그인", "회원가입"))
			.challengeTechStacks(List.of(spring))
			.build();
		em.persist(challenge);
		em.flush();
		em.clear();
	}
	
	@Test
	void search_challenge_where_name_like() {
	    // given
		LocalDateTime now = LocalDateTime.now();
		createChallenge("메모장 만들기"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		createChallenge("To-Do List"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, ADVANCED);
		ChallengeSearchCondition condition = ChallengeSearchCondition.builder()
			.searchLike("메모장")
			.build();
		PageRequest pageRequest = PageRequest.of(0, 5);
	    // when
		Slice<ChallengeSearchResult> results = challengeQueryRepository.findSlice(condition,
			pageRequest);
		// then
		List<ChallengeSearchResult> contents = results.getContent();
		ChallengeSearchResult challengeSearchResult = contents.get(0);
		assertThat(contents.size()).isEqualTo(1);
		assertThat(challengeSearchResult.getChallengeName()).isEqualTo("메모장 만들기");
	}

	@Test
	void search_challenge_where_tech_stack_like() {
	    // given
		LocalDateTime now = LocalDateTime.now();
		createChallenge("메모장 만들기"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		createChallenge("To-Do List"
			, List.of("Spring Boot", "react.js")
			, new Period(now, now.plusDays(5))
			, ADVANCED);
		ChallengeSearchCondition condition = ChallengeSearchCondition.builder()
			.searchLike("react")
			.build();
		PageRequest pageRequest = PageRequest.of(0, 5);
	    // when
		Slice<ChallengeSearchResult> results = challengeQueryRepository.findSlice(condition,
			pageRequest);
		// then
		List<ChallengeSearchResult> contents = results.getContent();
		ChallengeSearchResult challengeSearchResult = contents.get(0);
		assertThat(contents.size()).isEqualTo(1);
		assertThat(challengeSearchResult.getChallengeName()).isEqualTo("To-Do List");
	}

	private void createChallenge(String challengeName, List<String> stacks, Period recruitPeriod,
		ChallengeLevel challengeLevel) {

		LocalDateTime now = LocalDateTime.now();
		Recruit recruit = Recruit.builder()
			.number(5)
			.period(recruitPeriod)
			.type(FIRST_COME_FIRST_SERVE)
			.build();

		List<ChallengeTechStack> techStacks = stacks.stream()
			.map(ChallengeTechStack::new)
			.toList();

		Challenge challenge = Challenge.builder()
			.challengeName(challengeName)
			.level(challengeLevel)
			.period(new Period(now, now.plusDays(5)))
			.recruit(recruit)
			.funcRequirements(List.of("로그인", "회원가입"))
			.challengeTechStacks(techStacks)
			.build();
		em.persist(challenge);
		em.flush();
		em.clear();
	}

	@Test
	void search_challenge_order_by_name_desc() throws InterruptedException {
		// given
		LocalDateTime now = LocalDateTime.now();
		createChallenge("1"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		Thread.sleep(10);
		createChallenge("2"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		Thread.sleep(10);
		createChallenge("3"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		Thread.sleep(10);
		createChallenge("4"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		ChallengeSearchCondition condition = ChallengeSearchCondition.builder()
			.build();
		Order createDateDesc = Order.desc("challengeName");
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(createDateDesc));
		// when
		Slice<ChallengeSearchResult> results = challengeQueryRepository.findSlice(condition,
			pageRequest);
		// then
		List<ChallengeSearchResult> contents = results.getContent();
		ChallengeSearchResult challengeSearchResult = contents.get(0);
		assertThat(challengeSearchResult.getChallengeName()).isEqualTo("4");
	}
	
	@Test
	void search_challenge_order_by_default_created_date_desc() {
		// given
		LocalDateTime now = LocalDateTime.now();
		createChallenge("1"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		createChallenge("2"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		createChallenge("4"
			, List.of("Spring Boot", "Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		createChallenge("3"
			, List.of("Spring Boot", " m Vue.js")
			, new Period(now, now.plusDays(5))
			, BEGINNER);
		ChallengeSearchCondition condition = ChallengeSearchCondition.builder()
			.build();
		Order createDateDesc = Order.desc("notField");
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(createDateDesc));
		// when
		Slice<ChallengeSearchResult> results = challengeQueryRepository.findSlice(condition,
			pageRequest);
		// then
		List<ChallengeSearchResult> contents = results.getContent();
		ChallengeSearchResult challengeSearchResult = contents.get(0);
		assertThat(challengeSearchResult.getChallengeName()).isEqualTo("3");
	}

}