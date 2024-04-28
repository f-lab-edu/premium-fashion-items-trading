package com.inturn.pfit.global.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDTO {
	private Integer status;

	private String defaultErrorMessage;

	public static ErrorDTO createErrorDTO(Integer status, String defaultErrorMessage) {
		return ErrorDTO.builder()
				.status(status)
				.defaultErrorMessage(defaultErrorMessage)
				.build();
	}
}
