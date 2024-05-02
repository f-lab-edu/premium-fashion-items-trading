package com.inturn.pfit.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpResponseDTO {

	private Long userId;

	public SignUpResponseDTO(Long userId) {
		this.userId = userId;
	}
}
