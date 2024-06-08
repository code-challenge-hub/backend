package com.cch.codechallengehub.domain;


import com.cch.codechallengehub.constants.ChallengeLevel;
import com.cch.codechallengehub.constants.ChallengeStatus;
import com.cch.codechallengehub.constants.RecruitType;
import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
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

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "end_date")
	private LocalDateTime endDate;

	@Column(name = "recruit_type")
	@Enumerated(EnumType.STRING)
	private RecruitType recruitType;

	@Column(name = "recruit_start_date")
	private LocalDateTime recruitStartDate;

	@Column(name = "recruit_end_date")
	private LocalDateTime recruitEndDate;

	@Column(name = "recruit_number")
	private Integer recruitNumber;

	@Column(name = "func_requirements", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	private List<String> funcRequirements = new ArrayList<>();

	@Column(name = "view_count")
	private Long viewCount;

	@Column(name = "thumbnail")
	@Lob
	private byte[] thumbnail;

}
