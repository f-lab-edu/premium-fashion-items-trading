package com.inturn.pfit.global.common.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorCodeDTO {
	private HttpStatus status;

	private String defaultErrorMessage;

	public Integer getStatusValue() {
		return status.value();
	}

	public static ErrorCodeDTO createErrorDTO(HttpStatus status, String defaultErrorMessage) {
		return ErrorCodeDTO.builder()
				.status(status)
				.defaultErrorMessage(defaultErrorMessage)
				.build();
	}
}
