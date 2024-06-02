package com.inturn.pfit.support.fixture;

import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.config.security.vo.SessionConsts;
import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@UtilityClass
public class SessionFixture {

	static String email = "abc@naver.com";
	static String password = "@Abc.com1!";
	static String userName = "abc";
	static String userPhone = "01012345678";
	static Long userId = 1L;

	public static UserEntity getUserEntity(Long userId) {
		return UserEntity.builder()
				.userId(userId)
				.userName(userName)
				.userPhone(userPhone)
				.email(email)
				.password(password)
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	public static UserEntity getUserEntity(String roleCode) {
		return UserEntity.builder()
				.userId(userId)
				.userName(userName)
				.userPhone(userPhone)
				.email(email)
				.password(password)
				.roleCode(roleCode)
				.build();
	}

	public static void setMockSession(String roleCode) {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, new UserSession(getUserEntity(roleCode)));

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	public static void setMockSession() {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, null);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}


	public static MockHttpSession setMockSession(UserSession userSession) {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, userSession);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

		return session;
	}
}
