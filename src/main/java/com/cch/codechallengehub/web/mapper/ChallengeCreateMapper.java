package com.cch.codechallengehub.web.mapper;

import com.cch.codechallengehub.domain.Period;
import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.dto.ChallengeTechStackCreateDto;
import com.cch.codechallengehub.web.dto.ChallengeCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ChallengeCreateMapper {

	@Mapping(target = "techStacks", qualifiedByName = "techStackToDto")
	@Mapping(target = "period",
			expression = "java(createPeriod(request.getStartDate(), request.getEndDate()))")
	@Mapping(target = "recruitPeriod",
			expression = "java(createPeriod(request.getRecruitStartDate(), request.getRecruitEndDate()))")
	ChallengeCreateDto requestToDto(ChallengeCreateRequest request);

	@Named("techStackToDto")
	default ChallengeTechStackCreateDto techStacksToDto(String techStack) {
		return new ChallengeTechStackCreateDto(techStack);
	}

	default Period createPeriod(LocalDateTime startDate, LocalDateTime endDate) {
		return (startDate != null && endDate != null) ? new Period(startDate, endDate) : null;
	}

}
