package com.inturn.pfit.domain.category.exception;

import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class NotFoundCategoryException extends NotFoundException {

	public NotFoundCategoryException() {
		super(CategoryErrorCode.NOT_FOUND_CATEGORY_EXCEPTION.getError());
	}
}
