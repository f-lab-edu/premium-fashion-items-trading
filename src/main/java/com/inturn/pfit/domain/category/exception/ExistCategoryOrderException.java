package com.inturn.pfit.domain.category.exception;

import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public final class ExistCategoryOrderException extends PfitException {

	public ExistCategoryOrderException() {
		super(CategoryErrorCode.EXIST_CATEGORY_SORT_EXCEPTION.getError());
	}
}
