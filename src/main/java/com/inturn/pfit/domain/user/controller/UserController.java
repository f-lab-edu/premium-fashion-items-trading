package com.inturn.pfit.domain.user.controller;

import com.inturn.pfit.domain.user.dto.request.ChangeUserInfoRequestDTO;
import com.inturn.pfit.domain.user.dto.request.PasswordChangeRequestDTO;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.user.usecase.SignUpService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import com.inturn.pfit.global.config.security.define.RoleConsts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final SignUpService signUpService;

	private final UserQueryService userQueryService;

	private final UserCommandService userCommandService;

	//사용자 등록
	@PostMapping("/signUp")
	public ResponseEntity<DataResponseDTO<SignUpResponseDTO>> signUp(@RequestBody @Valid SignUpRequestDTO req) {
		//사용자 등록 처리
		return ResponseEntity.ok(new DataResponseDTO<>(signUpService.signUp(req)));
	}

	//사용자 중복 확인
	@GetMapping("/check/{email}")
	public ResponseEntity<CommonResponseDTO> duplicateUser(@PathVariable @NotEmpty @Email String email) {
		return ResponseEntity.ok(userQueryService.duplicateUser(email));
	}

	//사용자 조회
	@Secured(RoleConsts.ROLE_USER)
	@GetMapping
	public ResponseEntity<DataResponseDTO<UserResponseDTO>> getUser() {
		return ResponseEntity.ok(new DataResponseDTO<>(userQueryService.getUserBySession()));
	}

	//사용자 편집
	@Secured(RoleConsts.ROLE_USER)
	@PatchMapping("/info")
	public ResponseEntity<DataResponseDTO<UserResponseDTO>> changeUserInfo(@RequestBody @Valid ChangeUserInfoRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(userCommandService.changeUserInfo(req)));
	}

	//사용자 패스워드 변경
	@Secured(RoleConsts.ROLE_USER)
	@PatchMapping("/password")
	public ResponseEntity<CommonResponseDTO> changePassword(@RequestBody @Valid PasswordChangeRequestDTO req) {
		return ResponseEntity.ok(userCommandService.passwordChange(req));
	}

}
