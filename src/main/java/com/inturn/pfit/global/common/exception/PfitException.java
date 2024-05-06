package com.inturn.pfit.global.common.exception;

import com.inturn.pfit.global.common.dto.ErrorCodeDTO;
import lombok.Getter;

public class PfitException extends RuntimeException{

	@Getter
	private Integer status;
	protected PfitException(ErrorCodeDTO error) {
		super(error.getDefaultErrorMessage());
		this.status = error.getStatusValue();
	}
}
