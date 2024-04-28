package com.inturn.pfit.global.config.exception.handler;

import com.inturn.pfit.global.common.exception.PfitException;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;
import com.inturn.pfit.global.common.response.CommonResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class PfitExceptionHandler {

	//PftiExcpetion 이 발생되었을경우
	@ExceptionHandler(PfitException.class)
	public ResponseEntity<CommonResponseDTO> pfitExceptionHandler(PfitException e) {
		return ResponseEntity.status(e.getStatus()).body(CommonResponseDTO.failResponse(e.getStatus(), e.getMessage()));
	}

	//Validate 처리가 되었을 경우
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponseDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> fieldErrors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			if (error instanceof FieldError fieldError) {
				fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
		});
		return ResponseEntity.badRequest().body(CommonResponseDTO.failResponse(HttpStatus.BAD_REQUEST.value(), fieldErrors.toString()));
	}


	//올바르지 않은 요청이 수신되었을 경우
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CommonResponseDTO> exceptionHandler(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body(CommonResponseDTO.failResponse(ECommonErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getError()));
	}
}
