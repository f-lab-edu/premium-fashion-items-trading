package com.inturn.pfit.domain.address.exception;

import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class AddressNotFoundException extends NotFoundException {

	public AddressNotFoundException() {
		super(CategoryErrorCode.CATEGORY_NOT_FOUND_EXCEPTION.getError());
	}
}
