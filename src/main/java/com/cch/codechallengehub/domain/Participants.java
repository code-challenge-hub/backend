package com.cch.codechallengehub.domain;

import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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
	private String role;

	@Column(name = "position")
	private String position;

}
