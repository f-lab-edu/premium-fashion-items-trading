package com.inturn.pfit.domain.user.facade;

import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.userrole.entity.UserRole;
import com.inturn.pfit.domain.userrole.service.UserRoleQueryService;
import com.inturn.pfit.global.config.security.define.RoleConsts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class SignUpFacadeTest {

	@InjectMocks
	SignUpFacade signUpFacade;

	@Mock
	UserRoleQueryService userRoleQueryService;

	@Mock
	UserCommandService userCommandService;

	@Test
	@DisplayName("사용자 등록(singUp) - 성공")
	void singUp_Success() {
		//given
		SignUpRequestDTO req = createSignUpRequestDTO();

		Long userId = 1L;
		UserEntity user = getUser(userId, req);

		UserRole role = createUserRole();
		when(userRoleQueryService.getUserRoleByRoleCode(RoleConsts.ROLE_USER)).thenReturn(role);
		when(userCommandService.signUp(req, role)).thenReturn(user);

		//when
		SignUpResponseDTO res = signUpFacade.signUp(req);

		//then
		assertEquals(res.getUserId(), userId);

		verify(userCommandService, times(1)).signUp(req, role);
		verify(userRoleQueryService, times(1)).getUserRoleByRoleCode(RoleConsts.ROLE_USER);
	}

	@Test
	@DisplayName("사용자 등록(singUp) - Fail : Not Found Role")
	void singUp_Fail_NotFoundRole() {
		//given
		SignUpRequestDTO req = createSignUpRequestDTO();
		when(userRoleQueryService.getUserRoleByRoleCode(RoleConsts.ROLE_USER)).thenReturn(null);

		//when & then
		assertThrows(NullPointerException.class, () -> signUpFacade.signUp(req));

		verify(userRoleQueryService, times(1)).getUserRoleByRoleCode(RoleConsts.ROLE_USER);
	}

	private SignUpRequestDTO createSignUpRequestDTO() {
		return SignUpRequestDTO.builder()
				.email("abc@naver.com")
				.password("@Abc.com!")
				.confirmPassword("@Abc.com!")
				.alarmYn("Y")
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	private UserEntity getUser(Long userId, SignUpRequestDTO req) {
		return UserEntity.builder()
				.email(req.email())
				.password(req.password())
				.alarmYn(req.alarmYn())
				.roleCode(req.roleCode())
				.userId(userId)
				.build();
	}

	private UserRole createUserRole() {
		return UserRole.builder().roleCode(RoleConsts.ROLE_USER).build();
	}
}