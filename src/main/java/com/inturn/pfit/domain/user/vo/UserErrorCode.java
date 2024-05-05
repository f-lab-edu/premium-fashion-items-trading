package com.inturn.pfit.domain.user.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserErrorCode {

	EXIST_USER_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "해당 유저는 이미 등록되어 있습니다.")),
	PASSWORD_MISMATCH_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."))

	;
	private ErrorCodeDTO error;

	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}

	UserErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
