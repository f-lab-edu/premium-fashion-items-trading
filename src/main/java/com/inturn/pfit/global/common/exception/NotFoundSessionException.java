package com.inturn.pfit.global.common.exception;

import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;

public class NotFoundSessionException extends PfitException{

	public NotFoundSessionException() {
		super(ECommonErrorCode.NOT_FOUND_SESSION_EXCEPTION.getError());
	}
	//TODO - 파라미터를 넘겨받아 Message를 가공하는 부분도 추후 추가.

}
