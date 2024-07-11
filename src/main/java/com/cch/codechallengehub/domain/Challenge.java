package com.cch.codechallengehub.domain;


import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.converter.StringListToJsonConverter;
import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "challenge")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Challenge extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "challenge_id")
	private Long id;

	@Column(name = "challenge_name")
	private String challengeName;

	@Column(name = "level")
	@Enumerated(EnumType.STRING)
	private ChallengeLevel level;

	@Column(name = "challenge_desc")
	@Lob
	private String challengeDesc;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private ChallengeStatus status;

	@Embedded
	private Period period;

	@Embedded
	private Recruit recruit;

	@Column(name = "func_requirements", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	@Convert(converter = StringListToJsonConverter.class)
	private List<String> funcRequirements = new ArrayList<>();

	@Column(name = "view_count")
	private Long viewCount;

	@Column(name = "thumbnail")
	@Lob
	private byte[] thumbnail;

	@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengeTechStack> challengeTechStacks = new ArrayList<>();

	@OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChallengeQuest> challengeQuests = new ArrayList<>();

	@Builder
	public Challenge(String challengeName, ChallengeLevel level, String challengeDesc,
		Period period, Recruit recruit, List<String> funcRequirements, Long viewCount, byte[] thumbnail,
		List<ChallengeTechStack> challengeTechStacks, List<ChallengeQuest> challengeQuests,
		ChallengeStatus status) {

		setChallengeName(challengeName);
		setLevel(level);
		setPeriod(period);
		setRecruit(recruit);
		setChallengeTechStacks(challengeTechStacks);
		setFuncRequirements(funcRequirements);
		this.status = status;
		this.challengeDesc = challengeDesc;
		this.viewCount = viewCount;
		this.thumbnail = thumbnail;
		this.challengeQuests = challengeQuests;
	}

	private void setChallengeName(String challengeName) {
		notNull(challengeName, "Challenge name is not null!");
		this.challengeName = challengeName;
	}

	private void setLevel(ChallengeLevel level) {
		notNull(level, "Challenge level is not null!");
		this.level = level;
	}

	private void setPeriod(Period period) {
		notNull(period, "Challenge period is not null!");
		this.period = period;
	}

	private void setRecruit(Recruit recruit) {
		notNull(recruit, "Challenge recruit info is not null!");
		this.recruit = recruit;
	}

	private void setFuncRequirements(List<String> funcRequirements) {
		notNull(funcRequirements, "Challenge function requirements is not null!");
		this.funcRequirements = funcRequirements;
	}

	private void setChallengeTechStacks(
		List<ChallengeTechStack> challengeTechStacks) {
		notEmpty(challengeTechStacks, "Challenge tech stack is not null!");
		this.challengeTechStacks = challengeTechStacks;
	}

}
