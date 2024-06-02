package com.cch.codechallengehub.domain;

import com.cch.codechallengehub.constants.TechStackCategory;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tech_stack")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TechStack extends AuditingEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tech_stack_id")
	private Long id;

	@Column(name = "category")
	@Enumerated(EnumType.STRING)
	private TechStackCategory category;

	@Column(name = "stack_name")
	private String stackName;

	@Column(name = "icon")
	@Lob
	private byte[] icon;
}
