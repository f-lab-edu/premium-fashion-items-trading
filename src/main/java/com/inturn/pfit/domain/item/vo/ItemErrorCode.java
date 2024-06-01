package com.inturn.pfit.domain.item.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemErrorCode {

	ITEM_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."))
	;
	private ErrorCodeDTO error;

	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}

	public Integer getStatusValue() {
		return this.error.getStatusValue();
	}
}
