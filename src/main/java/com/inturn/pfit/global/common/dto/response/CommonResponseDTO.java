package com.inturn.pfit.global.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public class CommonResponseDTO {

	private Boolean success;
	private String message;

	public static CommonResponseDTO fail(String errorMessage) {
		return new CommonResponseDTO(false, errorMessage);
	}

	public static CommonResponseDTO ok() {
		return new CommonResponseDTO(true, StringUtils.EMPTY);
	}

	protected CommonResponseDTO() {
		this.success = true;
		this.message = StringUtils.EMPTY;
	}
}
