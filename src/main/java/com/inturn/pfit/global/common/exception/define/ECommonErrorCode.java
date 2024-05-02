package com.inturn.pfit.global.common.exception.define;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ECommonErrorCode {

	NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND.value(), "해당 데이터는 존재하지 않습니다.")),
	FORBIDDEN(ErrorCodeDTO.createErrorDTO(HttpStatus.FORBIDDEN.value(), "API 호출 권한이 없습니다.")),
	UNAUTHORIZED(ErrorCodeDTO.createErrorDTO(HttpStatus.UNAUTHORIZED.value(), "인증 정보가 존재하지 않습니다.")),
	HTTP_MESSAGE_NOT_READABLE_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST.value(), "요청 데이터가 올바르지 않습니다.")),
	NOT_FOUND_SESSION_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND.value(), "Session 정보가 존재하지 않습니다.")),
	;
	private ErrorCodeDTO error;

	ECommonErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
