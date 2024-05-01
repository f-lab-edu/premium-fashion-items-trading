package com.inturn.pfit.global.support.utils;

import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.config.security.define.SessionConsts;
import com.inturn.pfit.global.config.security.service.UserSession;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

//@UtilityClass는 기본 생성자가 private으로 생성되어 reflection 혹은 내부에서 생성자를 호출할 경우 UnsupportedOperationException 발생
@UtilityClass
public class SessionUtils {

	public static UserSession getUserSession(HttpSession session) {
		var userSesseion = session.getAttribute(SessionConsts.LOGIN_USER);
		if(ObjectUtils.isEmpty(userSesseion))   throw new NotFoundException();
		return (UserSession) session.getAttribute(SessionConsts.LOGIN_USER);
	}

	public static void setUserSession(HttpSession session, UserEntity user) {
		session.setAttribute(SessionConsts.LOGIN_USER, new UserSession(user));
	}

	public static void removeUserSession(HttpSession session) {
		session.removeAttribute(SessionConsts.LOGIN_USER);
	}
}
