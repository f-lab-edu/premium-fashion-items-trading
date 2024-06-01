package com.inturn.pfit.domain.itemsize.exception;

import com.inturn.pfit.domain.itemsize.vo.ItemSizeErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class ItemSizeNotFoundException extends NotFoundException {

	public ItemSizeNotFoundException() {
		super(ItemSizeErrorCode.ITEM_SIZE_NOT_FOUND_EXCEPTION.getError());
	}
}
