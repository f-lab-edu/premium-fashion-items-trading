package com.inturn.pfit.domain.user.exception;

import com.inturn.pfit.domain.user.define.EUserErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public class PasswordMismatchException extends PfitException {

	public PasswordMismatchException() {
		super(EUserErrorCode.PASSWORD_MISMATCH_EXCEPTION.getError());
	}

}
