package com.inturn.pfit.domain.size.exception;

import com.inturn.pfit.domain.size.vo.SizeErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class NotFoundSizeException extends NotFoundException {

	public NotFoundSizeException() {
		super(SizeErrorCode.NOT_FOUND_SIZE_EXCEPTION.getError());
	}
}
