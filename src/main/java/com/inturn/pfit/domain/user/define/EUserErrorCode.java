package com.inturn.pfit.domain.user.define;

import com.inturn.pfit.global.common.ErrorDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum EUserErrorCode {

	EXIST_USER_EXCEPTION(ErrorDTO.createErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "해당 유저는 이미 등록되어 있습니다."))

	;
	private ErrorDTO error;

	EUserErrorCode(ErrorDTO error) {
		this.error = error;
	}
}
