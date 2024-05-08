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
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.support.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCommandService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	@Transactional
	public UserEntity signUp(SignUpRequestDTO req, UserRole role) {
		//이미 등록된 email의 사용자 인지 확인
		Optional<UserEntity> userOpt = userRepository.findByEmail(req.email());
		if(userOpt.isPresent()){
			throw new ExistUserException();
		}
		if(!req.isCorrectPassword()) {
			throw new PasswordMismatchException();
		}
		return this.save(req.signUpUser(passwordEncoder));
	}

	@Transactional
	public UserResponseDTO changeUserInfo(ChangeUserInfoRequestDTO req) {
		UserEntity user = userRepository.findById(SessionUtils.getUserSession().getUserId()).orElseThrow(() -> new NotFoundException());
		UserEntity modUser = req.changeUserInfo(user);
		return UserResponseDTO.from(this.save(modUser));
	}

	@Transactional
	public CommonResponseDTO passwordChange(PasswordChangeRequestDTO req) {
		if(!req.isCorrectPassword()) {
			throw new PasswordMismatchException();
		}
		UserEntity user = userRepository.findById(SessionUtils.getUserSession().getUserId()).orElseThrow(() -> new NotFoundException());
		UserEntity changeUser = req.changePassword(user, passwordEncoder);
		this.save(changeUser);
		return CommonResponseDTO.ok();
	}

	@Transactional
	public UserEntity save(UserEntity entity) {
		return userRepository.save(entity);
	}
}
