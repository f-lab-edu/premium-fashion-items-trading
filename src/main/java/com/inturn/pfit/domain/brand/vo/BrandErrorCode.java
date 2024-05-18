package com.inturn.pfit.domain.brand.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BrandErrorCode {

	BRAND_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "해당 브랜드는 존재하지 않습니다."))

	;
	private ErrorCodeDTO error;


	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}


	public Integer getStatusValue() {
		return this.error.getStatusValue();
	}

	BrandErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
