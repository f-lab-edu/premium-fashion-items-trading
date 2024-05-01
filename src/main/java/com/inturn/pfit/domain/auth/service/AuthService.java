package com.inturn.pfit.domain.auth.service;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.PasswordMismatchException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.support.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
			throw new UsernameNotFoundException("해당 유저는 존재하지 않습니다.");
		}

		if(!passwordEncoder.matches(dto.password(), user.getPassword())) {
			throw new PasswordMismatchException();
		}

		//UserSession으로 set
		SessionUtils.setUserSession(session, user);
		return new CommonResponseDTO();
	}
}
