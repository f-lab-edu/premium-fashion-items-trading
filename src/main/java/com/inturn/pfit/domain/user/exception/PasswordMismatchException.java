package com.inturn.pfit.domain.user.exception;

import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.global.common.exception.PfitException;

public class PasswordMismatchException extends PfitException {


	public PasswordMismatchException() {
		super(UserErrorCode.PASSWORD_MISMATCH_EXCEPTION.getError());
	}
}
