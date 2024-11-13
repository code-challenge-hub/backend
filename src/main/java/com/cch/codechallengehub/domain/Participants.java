package com.cch.codechallengehub.domain;

import com.cch.codechallengehub.constants.ParticipantsRole;
import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "participants")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Participants extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "participants_id")
	private Long id;

	@JoinColumn(name = "team_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Team team;

	@JoinColumn(name = "challenge_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Challenge challenge;

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private ParticipantsRole role;

	@Column(name = "position")
	private String position;

	@Builder
	public Participants(User user, ParticipantsRole role, String position) {
		this.user = user;
		this.role = role;
		this.position = position;
	}

}
