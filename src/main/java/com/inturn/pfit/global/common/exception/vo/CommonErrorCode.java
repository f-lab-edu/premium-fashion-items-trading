package com.inturn.pfit.global.common.exception.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommonErrorCode {

	NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "해당 데이터는 존재하지 않습니다.")),
	FORBIDDEN(ErrorCodeDTO.createErrorDTO(HttpStatus.FORBIDDEN, "API 호출 권한이 없습니다.")),
	UNAUTHORIZED(ErrorCodeDTO.createErrorDTO(HttpStatus.UNAUTHORIZED, "인증 정보가 존재하지 않습니다.")),
	HTTP_MESSAGE_NOT_READABLE_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "요청 데이터가 올바르지 않습니다.")),
	NOT_FOUND_SESSION_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "Session 정보가 존재하지 않습니다.")),
	;
	final private ErrorCodeDTO error;

	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}

	CommonErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
