package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.support.utils.SessionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public Optional<UserEntity> getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Transactional(readOnly = true)
	public UserEntity getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException());
	}

	@Transactional(readOnly = true)
	public UserResponseDTO getUserBySession() {
		return UserResponseDTO.from(this.getUserById(SessionUtils.getUserSession().getUserId()));
	}

	@Transactional(readOnly = true)
	public boolean duplicateUser(String email) {
		Optional<UserEntity> userOpt = this.getUserByEmail(email);
		return userOpt.isPresent() ? true : false;
	}
}
