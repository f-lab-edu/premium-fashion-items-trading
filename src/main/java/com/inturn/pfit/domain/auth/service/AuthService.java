package com.inturn.pfit.domain.auth.service;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.PasswordMismatchException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;
import com.inturn.pfit.global.support.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService  {

	private final UserQueryService userQueryService;

	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public CommonResponseDTO login(LoginRequestDTO dto){

		UserEntity user = userQueryService.getUserByEmail(dto.email());
		if(ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException(ECommonErrorCode.UNAUTHORIZED.getError().getDefaultErrorMessage());
		}

		if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
			throw new PasswordMismatchException();
		}

		//UserSession으로 set
		SessionUtils.setUserSession(user);
		return new CommonResponseDTO();
	}
}
