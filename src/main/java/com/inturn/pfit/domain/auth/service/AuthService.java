package com.inturn.pfit.domain.auth.service;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.PasswordMismatchException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;
import com.inturn.pfit.global.support.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService  {

	private final UserQueryService userQueryService;

	private final PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public CommonResponseDTO login(LoginRequestDTO dto){

		Optional<UserEntity> userOpt = userQueryService.getUserByEmail(dto.email());
		if(userOpt.isEmpty()) {
			throw new UsernameNotFoundException(ECommonErrorCode.UNAUTHORIZED.getErrorMessage());
		}

		UserEntity user = userOpt.get();
		if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
			throw new PasswordMismatchException();
		}

		//UserSession으로 set
		SessionUtils.setUserSession(user);
		return CommonResponseDTO.ok();
	}

	public void logout() {
		SessionUtils.removeUserSession();
	}
}
