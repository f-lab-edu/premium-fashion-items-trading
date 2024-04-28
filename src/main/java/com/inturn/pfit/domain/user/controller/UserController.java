package com.inturn.pfit.domain.user.controller;

import com.inturn.pfit.domain.user.dto.SignUpRequestDTO;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.user.usecase.SignUpService;
import com.inturn.pfit.global.common.response.CommonResponseDTO;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	//service 호출
	private final SignUpService signUpService;

	//사용자 등록 요청
	@PostMapping("/signUp")
	public ResponseEntity<CommonResponseDTO> signUp(@RequestBody @Valid SignUpRequestDTO req) {
		//사용자 등록 처리
		return ResponseEntity.ok(signUpService.signUp(req));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/name")
	public ResponseEntity<String> getName() {
		//사용자 등록 처리
		return ResponseEntity.ok("test");
	}

	@Secured("ROLE_USER")
	@GetMapping("/user")
	public ResponseEntity<String> getUser() {
		//사용자 등록 처리
		return ResponseEntity.ok("test");
	}

}
