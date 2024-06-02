package com.inturn.pfit.domain.address.exception;

import com.inturn.pfit.domain.address.vo.AddressErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;

public final class AddressNotFoundException extends NotFoundException {

	public AddressNotFoundException() {
		super(AddressErrorCode.ADDRESS_NOT_FOUND_EXCEPTION.getError());
	}
}
