package com.inturn.pfit.global.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorCodeDTO {
	private Integer status;

	private String defaultErrorMessage;

	public static ErrorCodeDTO createErrorDTO(Integer status, String defaultErrorMessage) {
		return ErrorCodeDTO.builder()
				.status(status)
				.defaultErrorMessage(defaultErrorMessage)
				.build();
	}
}
