package com.inturn.pfit.global.config.exception.handler;

import com.inturn.pfit.global.common.exception.PfitException;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class PfitExceptionHandler {

	//PftiExcpetion 이 발생되었을경우
	@ExceptionHandler(PfitException.class)
	public ResponseEntity<CommonResponseDTO> pfitExceptionHandler(PfitException e) {
		return ResponseEntity.status(e.getStatus()).body(new CommonResponseDTO(e.getMessage()));
	}

	//Validate 처리가 되었을 경우
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<CommonResponseDTO> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		Map<String, String> errors = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(error -> {
			if (error instanceof FieldError fieldError) {
				errors.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
		});
		return ResponseEntity.badRequest().body(new CommonResponseDTO(errors.toString()));
	}

	//HandlerMethodValidationException 은 controller method parameter type (예: @RequestParameter, @PathVariable 등)에 따라 validation error를 처리
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<CommonResponseDTO> methodArgumentNotValidException(HandlerMethodValidationException e) {
		List<String> errorList = new ArrayList<>();
		e.getAllValidationResults().forEach(error -> {
			error.getResolvableErrors().forEach(messageSourceResolvable -> errorList.add(messageSourceResolvable.getDefaultMessage()));
		});
		return ResponseEntity.badRequest().body(new CommonResponseDTO(errorList.stream().collect(Collectors.joining(", "))));
	}


	//올바르지 않은 요청이 수신되었을 경우
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CommonResponseDTO> exceptionHandler(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body(new CommonResponseDTO(ECommonErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getError().getDefaultErrorMessage()));
	}
}
