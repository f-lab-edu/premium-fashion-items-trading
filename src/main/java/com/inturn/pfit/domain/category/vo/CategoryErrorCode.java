package com.inturn.pfit.domain.category.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CategoryErrorCode {

	EXIST_CATEGORY_SORT_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "해당 카테고리 순번은 이미 등록되어 있습니다.")),
	CATEGORY_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "해당 카테고리는 존재하지 않습니다."))

	;
	private ErrorCodeDTO error;


	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}


	public Integer getStatusValue() {
		return this.error.getStatusValue();
	}

	CategoryErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
