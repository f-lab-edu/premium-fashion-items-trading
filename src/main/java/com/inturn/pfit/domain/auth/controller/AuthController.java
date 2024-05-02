package com.inturn.pfit.domain.auth.controller;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.auth.service.AuthService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.support.utils.SessionUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/*
	컨트롤러 메소드의 객체를 만들어주는 ArgumentResolver가 동작하는데 @Valid도 해당 Resolver를 통해 처리된다.
	@RequestBody는 Json 메세지를 객체로 변환해주는 작업을 ArgumentResolver의 구현체인 RequestReponseBodyMethodProcess가 처리하며
	@Valid 어노테이션이 있을 경우 유효성 검사를 진행한다.
	검증에 오류가 발생할 경우 MethodArgumentNotValidException 예외가 발생하고, 400 BadRequest 에러가 발생한다.
	 */
	@PostMapping("/login")
	public ResponseEntity<CommonResponseDTO> login(@RequestBody @Valid LoginRequestDTO user) {
		return ResponseEntity.ok(authService.login(user));
	}

	@PostMapping("/logout")
	public ResponseEntity<CommonResponseDTO> logout() {
		SessionUtils.removeUserSession();
		return ResponseEntity.ok(new CommonResponseDTO());
	}
}
