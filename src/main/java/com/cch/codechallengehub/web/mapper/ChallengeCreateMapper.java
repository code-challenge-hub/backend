package com.cch.codechallengehub.web.mapper;

import com.cch.codechallengehub.dto.ChallengeCreateDto;
import com.cch.codechallengehub.dto.ChallengeTechStackCreateDto;
import com.cch.codechallengehub.web.dto.ChallengeCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface ChallengeCreateMapper {

	@Mapping(target = "techStacks", qualifiedByName = "techStackToDto")
	@Mapping(target = "period",
			expression = "java(request.getStartDate() != null && request.getEndDate() != null ? "
				+ "new com.cch.codechallengehub.domain.Period(request.getStartDate(), request.getEndDate()) : null)")
	@Mapping(target = "recruitPeriod",
			expression = "java(request.getRecruitStartDate() != null && request.getRecruitEndDate() != null ?"
				+ "new com.cch.codechallengehub.domain.Period(request.getRecruitStartDate(), request.getRecruitEndDate()) : null)")
	ChallengeCreateDto requestToDto(ChallengeCreateRequest request);

	@Named("techStackToDto")
	default ChallengeTechStackCreateDto techStacksToDto(String techStack) {
		return new ChallengeTechStackCreateDto(techStack);
	}

}
