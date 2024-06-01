package com.inturn.pfit.domain.brand.exception;

import com.inturn.pfit.domain.brand.vo.BrandErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class BrandNotFoundException extends NotFoundException {

	public BrandNotFoundException() {
		super(BrandErrorCode.BRAND_NOT_FOUND_EXCEPTION.getError());
	}
}
