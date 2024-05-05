package com.inturn.pfit.domain.auth.controller;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.auth.service.AuthService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
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

	@PostMapping("/v1/login")
	public ResponseEntity<CommonResponseDTO> loginV1(@RequestBody @Valid LoginRequestDTO user) {
		return ResponseEntity.ok(authService.login(user));
	}

	@PostMapping("/v1/logout")
	public ResponseEntity<CommonResponseDTO> logoutV1() {
		authService.logout();
		return ResponseEntity.ok(CommonResponseDTO.ok());
	}
}
