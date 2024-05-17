package com.inturn.pfit.domain.auth.service;

import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.PasswordMismatchException;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.config.security.vo.SessionConsts;
import com.inturn.pfit.support.vo.TestTypeConsts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@Tag(TestTypeConsts.UNIT_TEST)
class AuthServiceTest {

	@InjectMocks
	AuthService authService;

	@Mock
	UserQueryService userQueryService;

	@Spy
	BCryptPasswordEncoder passwordEncoder;

	UserEntity user;

	String email;
	String password;

	@BeforeEach
	void before() {
		email = "abc@naver.com";
		password = "@Abc.com!1";
		user = UserEntity.builder()
				.userId(1L)
				.userName("abc")
				.userPhone("01012345678")
				.email(email)
				.password(passwordEncoder.encode(password))
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	@Test
	@DisplayName("로그인(login) - 성공")
	void login_Success() {

		//given
		LoginRequestDTO req = LoginRequestDTO.builder()
				.email(email)
				.password(password)
				.build();

		//set mock session
		setMockSession(new UserSession(user));
		when(userQueryService.getUserByEmail(email)).thenReturn(Optional.of(user));

		//when
		CommonResponseDTO res = authService.login(req);

		//then
		assertEquals(res.getSuccess(), true);

		//verify
		verify(userQueryService, times(1)).getUserByEmail(email);
	}

	private void setMockSession(UserSession userSession) {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, userSession);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	@DisplayName("로그인(login) - 실패 : 존재하지 않는 사용자")
	void login_Fail_NotFoundUser() {

		//given
		LoginRequestDTO req = LoginRequestDTO.builder()
				.email(email)
				.password(password)
				.build();

		//set mock session
		when(userQueryService.getUserByEmail(email)).thenReturn(Optional.empty());

		//when & then
		UsernameNotFoundException result = assertThrows(UsernameNotFoundException.class, () -> authService.login(req));

		assertEquals(result.getMessage(), CommonErrorCode.UNAUTHORIZED.getErrorMessage());

		//verify
		verify(userQueryService, times(1)).getUserByEmail(email);
	}

	@Test
	@DisplayName("로그인(login) - 실패 : 암호 불일치")
	void login_Fail_NotMatchPassword() {

		//given
		LoginRequestDTO req = LoginRequestDTO.builder()
				.email(email)
				.password(password + "misMatch" )
				.build();

		when(userQueryService.getUserByEmail(email)).thenReturn(Optional.of(user));

		//when & then
		PasswordMismatchException result = assertThrows(PasswordMismatchException.class, () -> authService.login(req));

		assertEquals(result.getMessage(), UserErrorCode.PASSWORD_MISMATCH_EXCEPTION.getErrorMessage());

		//verify
		verify(userQueryService, times(1)).getUserByEmail(email);
	}

}