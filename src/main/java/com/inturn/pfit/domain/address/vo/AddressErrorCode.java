package com.inturn.pfit.domain.address.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum AddressErrorCode {

	ADDRESS_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "해당 주소는 존재하지 않습니다.")),
	USER_NOT_EQUALS_ADDRESS(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "조회할 주소는 현재 사용자의 주소가 아닙니다.")),
	;
	private ErrorCodeDTO error;

	AddressErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}

}
