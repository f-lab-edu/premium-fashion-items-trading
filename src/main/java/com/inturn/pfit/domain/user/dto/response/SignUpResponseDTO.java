package com.inturn.pfit.domain.user.dto.response;

import com.inturn.pfit.domain.user.entity.UserEntity;
import lombok.Builder;


@Builder
public record SignUpResponseDTO(
		Long userId
) {

	public static SignUpResponseDTO from(UserEntity user) {
		return SignUpResponseDTO.builder()
				.userId(user.getUserId())
				.build();
	}
}
