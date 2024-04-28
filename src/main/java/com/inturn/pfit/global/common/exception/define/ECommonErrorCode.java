package com.inturn.pfit.global.common.exception.define;

import com.inturn.pfit.global.common.ErrorDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ECommonErrorCode {

	NOT_FOUND_EXCEPTION(ErrorDTO.createErrorDTO(HttpStatus.NOT_FOUND.value(), "해당 데이터는 존재하지 않습니다.")),

	FORBIDDEN(ErrorDTO.createErrorDTO(HttpStatus.FORBIDDEN.value(), "API 호출 권한이 없습니다.")),
	UNAUTHORIZED(ErrorDTO.createErrorDTO(HttpStatus.UNAUTHORIZED.value(), "인증 정보가 존재하지 않습니다.")),
	HTTP_MESSAGE_NOT_READABLE_EXCEPTION(ErrorDTO.createErrorDTO(HttpStatus.BAD_REQUEST.value(), "요청 데이터가 올바르지 않습니다.")),
	;
	private ErrorDTO error;

	ECommonErrorCode(ErrorDTO error) {
		this.error = error;
	}
}
