package com.inturn.pfit.global.common.exception;

import com.inturn.pfit.global.common.ErrorDTO;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;

public class NotFoundException extends PfitException{

	public NotFoundException(ErrorDTO errorCode) {
		super(errorCode);
	}
	//TODO - 파라미터가 존재하는 유형 확인

}
