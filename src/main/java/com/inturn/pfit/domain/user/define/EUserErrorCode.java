package com.inturn.pfit.domain.user.define;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EUserErrorCode {

	EXIST_USER_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST.value(), "해당 유저는 이미 등록되어 있습니다.")),
	PASSWORD_MISMATCH_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST.value(), "비밀번호가 일치하지 않습니다."))

	;
	private ErrorCodeDTO error;

	EUserErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
