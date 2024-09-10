package com.cch.codechallengehub.domain;

import com.cch.codechallengehub.entity.AuditingEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "user_id")
	private String id;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@OneToOne(mappedBy = "user"
		, cascade = CascadeType.ALL
		, orphanRemoval = true)
	private Profile profile;

	private String role;

	@Builder
	public User(String nickname, String email, String password) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
	}

	@Builder
	public User(String nickname, String email, String password, String role) {
		this.nickname = nickname;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	@Builder
	public User(String email, String role) {
		this.email = email;
		this.role = role;
	}

	public void changePassword(String password) {
		this.password = password;
	}

}
