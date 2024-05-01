package com.inturn.pfit.domain.user.exception;

import com.inturn.pfit.domain.user.define.EUserErrorCode;
import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import com.inturn.pfit.global.common.exception.PfitException;

//final 로 해당 Exception을 통한 상속 불가
public final class ExistUserException extends PfitException {

	public ExistUserException() {
		super(EUserErrorCode.EXIST_USER_EXCEPTION.getError());
	}

	public ExistUserException(ErrorCodeDTO error) {
		super(error);
	}
}
