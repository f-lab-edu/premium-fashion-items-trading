package com.inturn.pfit.domain.sizetype.exception;

import com.inturn.pfit.domain.sizetype.vo.SizeTypeErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public class DuplicateSizeTypeOrderException extends PfitException {

	public DuplicateSizeTypeOrderException() {
		super(SizeTypeErrorCode.DUPLICATE_SIZE_TYPE_EXCEPTION.getError());
	}
}
