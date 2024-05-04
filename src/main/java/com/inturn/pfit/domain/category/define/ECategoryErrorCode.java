package com.inturn.pfit.domain.category.define;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ECategoryErrorCode {

	EXIST_CATEGORY_SORT_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST.value(), "해당 카테고리 순번은 이미 등록되어 있습니다."))

	;
	private ErrorCodeDTO error;

	ECategoryErrorCode(ErrorCodeDTO error) {
		this.error = error;
	}
}
