package com.inturn.pfit.domain.sizetype.exception;

import com.inturn.pfit.domain.sizetype.vo.SizeTypeErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class NotFoundSizeTypeException extends NotFoundException {

	public NotFoundSizeTypeException() {
		super(SizeTypeErrorCode.NOT_FOUND_SIZE_TYPE_EXCEPTION.getError());
	}
}
