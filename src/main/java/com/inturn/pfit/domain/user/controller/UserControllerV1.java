package com.inturn.pfit.domain.user.controller;

import com.inturn.pfit.domain.user.dto.request.ChangeUserInfoRequestDTO;
import com.inturn.pfit.domain.user.dto.request.PasswordChangeRequestDTO;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.facade.SignUpFacade;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class UserControllerV1 {

	private final SignUpFacade signUpFacade;

	private final UserQueryService userQueryService;

	private final UserCommandService userCommandService;

	//사용자 등록
	@PostMapping("/user/sign-up")
	public ResponseEntity<DataResponseDTO<SignUpResponseDTO>> signUpV1(@RequestBody @Valid SignUpRequestDTO req) {
		//사용자 등록 처리
		return ResponseEntity.ok(new DataResponseDTO<>(signUpFacade.signUp(req)));
	}

	//사용자 중복 확인
	@GetMapping("/user/check/{email}")
	public ResponseEntity<CommonResponseDTO> duplicateUserV1(@PathVariable @NotEmpty @Email String email) {
		return ResponseEntity.ok(userQueryService.duplicateUser(email) ? CommonResponseDTO.fail(UserErrorCode.EXIST_USER_EXCEPTION.getErrorMessage()): CommonResponseDTO.ok());
	}

	//사용자 조회
	@Secured(RoleConsts.ROLE_USER)
	@GetMapping("/user")
	public ResponseEntity<DataResponseDTO<UserResponseDTO>> getUserV1() {
		return ResponseEntity.ok(new DataResponseDTO<>(userQueryService.getUserBySession()));
	}

	//사용자 편집
	@Secured(RoleConsts.ROLE_USER)
	@PatchMapping("/user/info")
	public ResponseEntity<DataResponseDTO<UserResponseDTO>> changeUserInfoV1(@RequestBody @Valid ChangeUserInfoRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(userCommandService.changeUserInfo(req)));
	}

	//사용자 패스워드 변경
	@Secured(RoleConsts.ROLE_USER)
	@PatchMapping("/user/password")
	public ResponseEntity<CommonResponseDTO> changePasswordV1(@RequestBody @Valid PasswordChangeRequestDTO req) {
		return ResponseEntity.ok(userCommandService.passwordChange(req));
	}

}
