package com.inturn.pfit.domain.auth.service;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;
import com.inturn.pfit.global.common.response.CommonResponseDTO;
import com.inturn.pfit.global.config.security.define.SessionConst;
import com.inturn.pfit.global.config.security.service.UserSession;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService  {

	private final UserQueryService userQueryService;

	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public CommonResponseDTO login(LoginRequestDTO dto, HttpSession session){

		UserEntity user = userQueryService.getUserByEmail(dto.email());
		if(Objects.isNull(user)) {
			throw new NotFoundException(ECommonErrorCode.NOT_FOUND_EXCEPTION.getError());
		}

		if(passwordEncoder.matches(user.getPassword(), dto.password())) {
			//TODO Password 불일치 CustomException 생성
			throw new RuntimeException("패스워드가 일치하지 않습니다.");
		}

		//UserSession으로 가공해서 넣자.
		session.setAttribute(SessionConst.LOGIN_USER, new UserSession(user));
		return CommonResponseDTO.successResponse();
	}
}
