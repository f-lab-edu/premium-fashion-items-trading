package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.dto.request.ChangeUserInfoRequestDTO;
import com.inturn.pfit.domain.user.dto.request.PasswordChangeRequestDTO;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.ExistUserException;
import com.inturn.pfit.domain.user.exception.PasswordMismatchException;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.domain.userrole.entity.UserRole;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.support.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

	private final UserRepository userRepository;
	private final UserQueryService userQueryService;
	private final PasswordEncoder passwordEncoder;


	@Transactional
	public UserEntity signUp(SignUpRequestDTO req, UserRole role) {
		//이미 등록된 email의 사용자 인지 확인
		UserEntity user = userRepository.findByEmail(req.email());
		if(ObjectUtils.isNotEmpty(user)){
			throw new ExistUserException();
		}
		if(!req.isCorrectPassword()) {
			throw new PasswordMismatchException();
		}
		return this.save(UserEntity.signUpUser(req.email(), req.password(), req.alarmYn(), req.roleCode(), passwordEncoder));
	}

	@Transactional
	public UserResponseDTO changeUserInfo(ChangeUserInfoRequestDTO req, HttpSession session) {
		var user = userQueryService.getUserById(SessionUtils.getUserSession(session).getUserId());
		user.changeUserInfo(req.userPhone(), req.userName(), req.profileName(), req.profileUrl(), req.alarmYn());
		return UserResponseDTO.from(this.save(user));
	}

	@Transactional
	public CommonResponseDTO passwordChange(PasswordChangeRequestDTO req, HttpSession session) {
		var user = userQueryService.getUserById(SessionUtils.getUserSession(session).getUserId());
		if(!req.isCorrectPassword()) {
			throw new PasswordMismatchException();
		}
		user.changePassword(req.password(), passwordEncoder);
		return new CommonResponseDTO();
	}

	@Transactional
	public UserEntity save(UserEntity entity) {
		return userRepository.save(entity);
	}
}
