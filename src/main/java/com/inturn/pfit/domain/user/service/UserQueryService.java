package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.define.EUserErrorCode;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.support.utils.SessionUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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

	@Transactional(readOnly = true)
	public UserEntity getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new NotFoundException());
	}

	@Transactional(readOnly = true)
	public UserResponseDTO getUserBySession(HttpSession session) {
		return UserResponseDTO.from(getUserById(SessionUtils.getUserSession(session).getUserId()));
	}

	@Transactional(readOnly = true)
	public CommonResponseDTO duplicateUser(String email) {
		if(ObjectUtils.isNotEmpty(this.getUserByEmail(email))) {
			return new CommonResponseDTO(EUserErrorCode.EXIST_USER_EXCEPTION.getError().getDefaultErrorMessage());
		}
		return new CommonResponseDTO();
	}
}
