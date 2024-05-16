package com.inturn.pfit.domain.size.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SizeErrorCode {

	SIZE_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "사이즈 종류가 존재하지 않습니다."))
	;
	private ErrorCodeDTO error;

	//TODO - 아래로직 공통화 시킬 수 있는 방안.
	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}

	public Integer getStatusValue() {
		return this.error.getStatusValue();
	}
}
