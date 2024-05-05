package com.inturn.pfit.global.config.exception.handler;

import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.PfitException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
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

	//TODO - 아래 로직을 주석해제하고 ExceptionHandling 할 경우 403(FORBIDDEN)발생 시 아래 메소드에서 500 에러가 발생됨. 이유를 확인해보자.
	//Exception 이 발생하였을 경우
//	@ExceptionHandler(Exception.class)
//	public ResponseEntity<CommonResponseDTO> exceptionHandler(Exception e) {
//		return ResponseEntity.internalServerError().body(CommonResponseDTO.fail(e.toString()));
//	}

	//PftiExcpetion 이 발생되었을경우
	@ExceptionHandler(PfitException.class)
	public ResponseEntity<CommonResponseDTO> pfitExceptionHandler(PfitException e) {
		return ResponseEntity.status(e.getStatus()).body(CommonResponseDTO.fail(e.getMessage()));
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
		return ResponseEntity.badRequest().body(CommonResponseDTO.fail(errors.toString()));
	}

	//HandlerMethodValidationException 은 controller method parameter type (예: @RequestParameter, @PathVariable 등)에 따라 validation error를 처리
	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<CommonResponseDTO> methodArgumentNotValidException(HandlerMethodValidationException e) {
		List<String> errorList = new ArrayList<>();
		e.getAllValidationResults().forEach(error -> {
			error.getResolvableErrors().forEach(messageSourceResolvable -> errorList.add(messageSourceResolvable.getDefaultMessage()));
		});
		return ResponseEntity.badRequest().body(CommonResponseDTO.fail(errorList.stream().collect(Collectors.joining(", "))));
	}


	//올바르지 않은 요청이 수신되었을 경우
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<CommonResponseDTO> exceptionHandler(HttpMessageNotReadableException e) {
		return ResponseEntity.badRequest().body(CommonResponseDTO.fail(CommonErrorCode.HTTP_MESSAGE_NOT_READABLE_EXCEPTION.getErrorMessage()));
	}
}
