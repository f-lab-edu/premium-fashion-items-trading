package com.inturn.pfit.domain.item.exception;

import com.inturn.pfit.domain.item.vo.ItemErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class ItemNotFoundException extends NotFoundException {

	public ItemNotFoundException() {
		super(ItemErrorCode.ITEM_NOT_FOUND_EXCEPTION.getError());
	}
}
