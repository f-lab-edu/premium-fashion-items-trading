package com.inturn.pfit.domain.user.facade;

import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.userrole.entity.UserRole;
import com.inturn.pfit.domain.userrole.service.UserRoleQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpFacade {

	private final UserRoleQueryService userRoleQueryService;

	private final UserCommandService userCommandService;

	@Transactional
	public SignUpResponseDTO signUp(SignUpRequestDTO req) {
		//권한코드가 DB에 저장된 코드인지 확인
		UserRole role = userRoleQueryService.getUserRoleByRoleCode(req.roleCode());
		//사용자 등록 처리
		return new SignUpResponseDTO(userCommandService.signUp(req, role).getUserId());
	}
}
