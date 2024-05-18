package com.inturn.pfit.domain.itemsize.exception;

import com.inturn.pfit.domain.itemsize.vo.ItemSizeErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public class DuplicateItemSizeOrderException extends PfitException {

	public DuplicateItemSizeOrderException() {
		super(ItemSizeErrorCode.DUPLICATE_ITEM_SIZE_EXCEPTION.getError());
	}
}
