package com.inturn.pfit.global.common.dto.response;

import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class DataResponseDTO<T> extends CommonResponseDTO{

	private T data;

	public DataResponseDTO(T data) {
		super();
		this.data = data;
	}

	public static <T> ResponseEntity<DataResponseDTO<T>> ok(T data) {
		return ResponseEntity.ok(new DataResponseDTO<T>(data));
	}

}
