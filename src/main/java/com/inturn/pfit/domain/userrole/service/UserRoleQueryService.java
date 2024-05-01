package com.inturn.pfit.domain.userrole.service;

import com.inturn.pfit.domain.userrole.entity.UserRole;
import com.inturn.pfit.domain.userrole.repository.UserRoleRepository;
import com.inturn.pfit.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleQueryService {

	private final UserRoleRepository userRoleRepository;

	/**
	 * userRole을 조회, 데이터가 존재하지 않을 경우 NotFoundExcpeiton 발생.
	 * @param roleCode
	 * @return 데이터가 존재할 경우 UserRole 반환
	 */
	public UserRole getUserRoleByRoleCode(String roleCode) {
		return userRoleRepository.findById(roleCode).orElseThrow(() -> new NotFoundException());
	}
}
