package com.inturn.pfit.global.common.exception;

import com.inturn.pfit.global.common.ErrorDTO;
import lombok.Getter;

public class PfitException extends RuntimeException{

	@Getter
	private Integer status;
	protected PfitException(ErrorDTO error) {
		super(error.getDefaultErrorMessage());
		this.status = error.getStatus();
	}
}
