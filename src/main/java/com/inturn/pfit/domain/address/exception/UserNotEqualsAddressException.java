package com.inturn.pfit.domain.address.exception;

import com.inturn.pfit.domain.address.vo.AddressErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public class UserNotEqualsAddressException extends PfitException {

	public UserNotEqualsAddressException() {
		super(AddressErrorCode.USER_NOT_EQUALS_ADDRESS.getError());
	}
}
