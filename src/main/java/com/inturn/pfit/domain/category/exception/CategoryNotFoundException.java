package com.inturn.pfit.domain.category.exception;

import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class CategoryNotFoundException extends NotFoundException {

	public CategoryNotFoundException() {
		super(CategoryErrorCode.CATEGORY_NOT_FOUND_EXCEPTION.getError());
	}
}
