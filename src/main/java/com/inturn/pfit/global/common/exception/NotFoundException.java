package com.inturn.pfit.global.common.exception;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;

public class NotFoundException extends PfitException{

	public NotFoundException() {
		super(CommonErrorCode.NOT_FOUND_EXCEPTION.getError());
	}

	public NotFoundException(ErrorCodeDTO dto) {
		super(dto);
	}
	//TODO - 변수를 넘겨받아 Message를 가공하는 부분도 확인

}
