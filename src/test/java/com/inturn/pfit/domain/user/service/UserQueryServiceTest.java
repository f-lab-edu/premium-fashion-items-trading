package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.support.utils.SessionUtils;
import com.inturn.pfit.support.fixture.SessionFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
		userId = 1L;
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
		when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

		//when
		boolean result = userQueryService.duplicateUser(email);

		//then
		assertEquals(result, false);

		//verify
		verify(userRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("사용자 중복 확인(duplicateUser) - 실패 : 기 사용자 존재")
	void duplicateUser_Fail_ExistUser() {
		//given
		when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

		//when
		boolean result = userQueryService.duplicateUser(email);

		//then
		assertEquals(result, true);

		//verify
		verify(userRepository, times(1)).findByEmail(email);
	}

	@Test
	@DisplayName("사용자 조회(getUserBySession) - 성공")
	void getUserBySession_Success() {

		//given
		//setSession
		SessionFixture.setMockSession(RoleConsts.ROLE_USER);

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
		//null session
		SessionFixture.setMockSession();

		//when & then
		final NotFoundSessionException result = assertThrows(NotFoundSessionException.class, () -> userQueryService.getUserBySession());
		assertNotNull(result);
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_SESSION_EXCEPTION.getErrorMessage());

		//verify
		verify(userRepository, times(0)).findById(userId);
	}

//	private void setMockSession(UserSession userSession) {
//		MockHttpSession session = new MockHttpSession();
//		session.setAttribute(SessionConsts.LOGIN_USER, userSession);
//
//		MockHttpServletRequest request = new MockHttpServletRequest();
//		request.setSession(session);
//		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//	}
}