package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.define.EUserErrorCode;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.common.exception.define.ECommonErrorCode;
import com.inturn.pfit.global.config.security.define.RoleConsts;
import com.inturn.pfit.global.config.security.define.SessionConsts;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.support.utils.SessionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserQueryServiceTest {

	@InjectMocks
	UserQueryService userQueryService;

	@Mock
	UserRepository userRepository;

	UserEntity user;

	String email;

	Long userId;


	@BeforeEach
	void before(){
		email = "abc@naver.com";
		userId = 1l;
		user = UserEntity.builder()
				.email(email)
				.userId(userId)
				.roleCode(RoleConsts.ROLE_USER)
				.password("@Abc.com!")
				.build();
	}

	@Test
	@DisplayName("사용자 중복 확인(duplicateUser) - 성공")
	void duplicateUser_Success() {
		//given
		when(userRepository.findByEmail(email)).thenReturn(null);

		//when
		CommonResponseDTO res = userQueryService.duplicateUser(email);

		//then
		assertEquals(res.getSuccess(), true);

		//verify
		verify(userRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("사용자 중복 확인(duplicateUser) - 실패 : 기 사용자 존재")
	void duplicateUser_Fail_ExistUser() {
		//given
		when(userRepository.findByEmail(email)).thenReturn(user);

		//when
		CommonResponseDTO res = userQueryService.duplicateUser(email);

		//then
		assertEquals(res.getSuccess(), false);
		assertEquals(res.getMessage(), EUserErrorCode.EXIST_USER_EXCEPTION.getError().getDefaultErrorMessage());

		//verify
		verify(userRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("사용자 조회(getUserBySession) - 성공")
	void getUserBySession_Success() {

		//given
		//setSession
		setMockSession(new UserSession(user));

		Long userId = SessionUtils.getUserSession().getUserId();
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		//when
		UserResponseDTO res = userQueryService.getUserBySession();

		//then
		assertEquals(res.getUserId(), userId);

		//verify
		verify(userRepository, times(1)).findById(userId);
	}

	@Test
	@DisplayName("사용자 조회(getUserBySession) - 실패 : Session 정보 없음")
	void getUserBySession_Fail_NotFoundSession() {

		//given
		//setSession
		setMockSession(null);

		//when & then
		final NotFoundSessionException result = assertThrows(NotFoundSessionException.class, () -> userQueryService.getUserBySession());
		assertNotNull(result);
		assertEquals(result.getMessage(), ECommonErrorCode.NOT_FOUND_SESSION_EXCEPTION.getError().getDefaultErrorMessage());

		//verify
		verify(userRepository, times(0)).findById(userId);
	}

	private void setMockSession(UserSession userSession) {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, userSession);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}
}