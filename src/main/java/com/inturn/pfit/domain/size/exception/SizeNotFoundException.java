package com.inturn.pfit.domain.size.exception;

import com.inturn.pfit.domain.size.vo.SizeErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class SizeNotFoundException extends NotFoundException {

	public SizeNotFoundException() {
		super(SizeErrorCode.SIZE_NOT_FOUND_EXCEPTION.getError());
	}
}
