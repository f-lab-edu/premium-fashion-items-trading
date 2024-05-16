package com.inturn.pfit.domain.sizetype.exception;

import com.inturn.pfit.domain.sizetype.vo.SizeTypeErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class SizeTypeNotFoundException extends NotFoundException {

	public SizeTypeNotFoundException() {
		super(SizeTypeErrorCode.SIZE_TYPE_NOT_FOUND_EXCEPTION.getError());
	}
}
