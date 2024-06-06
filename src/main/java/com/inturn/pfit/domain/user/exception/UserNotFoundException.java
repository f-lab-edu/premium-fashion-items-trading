package com.inturn.pfit.domain.user.exception;

import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public final class UserNotFoundException extends PfitException {

	public UserNotFoundException() {
		super(UserErrorCode.USER_NOT_FOUND.getError());
	}

}
