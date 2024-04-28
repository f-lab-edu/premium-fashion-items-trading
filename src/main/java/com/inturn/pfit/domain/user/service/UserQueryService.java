package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.entity.User;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.ExistUserException;
import com.inturn.pfit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserQueryService {
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public UserEntity getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
