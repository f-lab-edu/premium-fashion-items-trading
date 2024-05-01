package com.inturn.pfit.global.common.exception;

import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;

public class NotFoundException extends PfitException{

	public NotFoundException() {
		super(ECommonErrorCode.NOT_FOUND_EXCEPTION.getError());
	}
	//TODO - 변수를 넘겨받아 Message를 가공하는 부분도 확인

}
