package com.inturn.pfit.domain.user.dto.response;

import lombok.Getter;

@Getter
public class SignUpResponseDTO {

	private Long userId;

	public SignUpResponseDTO(Long userId) {
		this.userId = userId;
	}
}
