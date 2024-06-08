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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "challenge_comment")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeComment extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "challenge_comment_id")
	private Long id;

	@JoinColumn(name = "challenge_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Challenge challenge;

	@JoinColumn(name = "parent_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private ChallengeComment parent;

	@OneToMany(mappedBy = "parent")
	private List<ChallengeComment> child = new ArrayList<>();

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Column(name = "comment")
	private String comment;

	@Column(name = "orders")
	private Integer orders;

}
