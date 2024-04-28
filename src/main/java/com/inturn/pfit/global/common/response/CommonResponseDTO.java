package com.inturn.pfit.global.common.response;

import com.inturn.pfit.global.common.ErrorDTO;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record CommonResponseDTO(
		Boolean success,
		Integer status,
		String error
) {
	public static CommonResponseDTO successResponse() {
		return CommonResponseDTO.builder().success(true).status(HttpStatus.OK.value()).build();
	}

	public static CommonResponseDTO failResponse(Integer status, String errorMessage) {
		return CommonResponseDTO.builder().success(false).status(status).error(errorMessage).build();
	}

	public static CommonResponseDTO failResponse(ErrorDTO error){
		return CommonResponseDTO.builder().success(false).status(error.getStatus()).error(error.getDefaultErrorMessage()).build();
	}

}
