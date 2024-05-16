package com.inturn.pfit.domain.sizetype.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SizeTypeErrorCode {

	SIZE_TYPE_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "사이즈 종류가 존재하지 않습니다.")),
	DUPLICATE_SIZE_TYPE_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "사이즈 종류의 순서가 중복일 수 없습니다."))

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
