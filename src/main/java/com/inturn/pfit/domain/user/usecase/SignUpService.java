package com.inturn.pfit.domain.user.usecase;

import com.inturn.pfit.domain.user.dto.SignUpRequestDTO;
import com.inturn.pfit.domain.user.entity.User;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.ExistUserException;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.userrole.entity.UserRole;
import com.inturn.pfit.domain.userrole.service.UserRoleQueryService;
import com.inturn.pfit.global.common.response.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SignUpService {

	private final UserRoleQueryService userRoleQueryService;

	private final UserCommandService userCommandService;

	private final UserQueryService userQueryService;

	@Transactional
	public CommonResponseDTO signUp(SignUpRequestDTO req) {
		//권한코드가 DB에 저장된 코드인지 확인
		UserRole role = userRoleQueryService.getUserRoleByRoleCode(req.roleCode());
		//사용자 등록 처리
		userCommandService.signUp(req);
		return CommonResponseDTO.successResponse();
	}
}
