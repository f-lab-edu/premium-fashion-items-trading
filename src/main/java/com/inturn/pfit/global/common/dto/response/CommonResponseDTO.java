package com.inturn.pfit.global.common.dto.response;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public class CommonResponseDTO {

	private Boolean success;
	private String message;

	public CommonResponseDTO(String errorMessage) {
		this.success = false;
		this.message = errorMessage;
	}

	public CommonResponseDTO() {
		this.success = true;
		this.message = StringUtils.EMPTY;
	}
}
