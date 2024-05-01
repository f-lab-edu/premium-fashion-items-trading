package com.inturn.pfit.global.common.dto.response;

import lombok.Getter;

@Getter
public class DataResponseDTO<T> extends CommonResponseDTO{

	private T data;

	public DataResponseDTO(T data) {
		super();
		this.data = data;
	}
}
