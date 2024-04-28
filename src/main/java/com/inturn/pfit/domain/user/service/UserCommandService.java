package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.define.EUserErrorCode;
import com.inturn.pfit.domain.user.dto.SignUpRequestDTO;
import com.inturn.pfit.domain.user.entity.User;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.ExistUserException;
import com.inturn.pfit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserCommandService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void signUp(SignUpRequestDTO req) {
		//이미 등록된 email의 사용자 인지 확인
		UserEntity user = userRepository.findByEmail(req.email());
		if(Objects.nonNull(user)){
			throw new ExistUserException(EUserErrorCode.EXIST_USER_EXCEPTION.getError());
		}
		var createUser = UserEntity.signUpUser(req.email(), req.password(), req.alarmYn(), req.roleCode(), passwordEncoder);
		this.save(createUser);

	}

	@Transactional
	public void save(User param) {
		userRepository.save(param);
	}
}
