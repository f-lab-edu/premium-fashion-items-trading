package com.inturn.pfit.domain.auth.controller;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.auth.service.AuthService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthV1Controller {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<CommonResponseDTO> login(@RequestBody @Valid LoginRequestDTO user) {
		return ResponseEntity.ok(authService.login(user));
	}

	@PostMapping("/logout")
	public ResponseEntity<CommonResponseDTO> logout() {
		authService.logout();
		return ResponseEntity.ok(CommonResponseDTO.ok());
	}
}
