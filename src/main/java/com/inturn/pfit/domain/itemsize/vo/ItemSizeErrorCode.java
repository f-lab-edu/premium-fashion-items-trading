package com.inturn.pfit.domain.itemsize.vo;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ItemSizeErrorCode {

	ITEM_SIZE_NOT_FOUND_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.NOT_FOUND, "상품 사이즈가 존재하지 않습니다.")),
	DUPLICATE_ITEM_SIZE_EXCEPTION(ErrorCodeDTO.createErrorDTO(HttpStatus.BAD_REQUEST, "상품 사이즈의 순서는 중복일 수 없습니다."));
	private ErrorCodeDTO error;

	public String getErrorMessage() {
		return this.error.getDefaultErrorMessage();
	}

	public Integer getStatusValue() {
		return this.error.getStatusValue();
	}
}
