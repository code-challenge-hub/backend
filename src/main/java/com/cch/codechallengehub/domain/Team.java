package com.cch.codechallengehub.domain;

import com.cch.codechallengehub.constants.TeamStatus;
import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "team_id")
	private Long id;

	@JoinColumn(name = "challenge_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Challenge challenge;

	@Column(name = "team_name")
	private String teamName;

	@Column(name = "overview")
	@Lob
	private String overview;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private TeamStatus status;

	@BatchSize(size = 10)
	@OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Participants> participants = new ArrayList<>();

	@Builder
	public Team(Challenge challenge, String teamName, String overview, TeamStatus status, List<Participants> participants) {
		this.challenge = challenge;
		this.teamName = teamName;
		this.overview = overview;
		this.status = status;
		this.participants = participants;
	}

	public void updateOverview(String overview) {
		this.overview = overview;
	}

	public void updateStatus(TeamStatus status) {
		this.status = status;
	}

}
