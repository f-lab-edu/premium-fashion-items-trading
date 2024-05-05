package com.inturn.pfit.global.support.utils;

import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.vo.SessionConsts;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@UtilityClass
public class SessionUtils {

	public static UserSession getUserSession() {
		HttpSession session = getSession();
		var userSesseion = session.getAttribute(SessionConsts.LOGIN_USER);
		if(ObjectUtils.isEmpty(userSesseion))   throw new NotFoundSessionException();
		return (UserSession) session.getAttribute(SessionConsts.LOGIN_USER);
	}

	public static void setUserSession(UserEntity user) {
		HttpSession session = getSession();
		session.setAttribute(SessionConsts.LOGIN_USER, new UserSession(user));
	}

	public static void removeUserSession() {
		HttpSession session = getSession();
		session.removeAttribute(SessionConsts.LOGIN_USER);
	}

	public static HttpSession getSession(){
		ServletRequestAttributes servletRA = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = servletRA.getRequest().getSession();

		if(ObjectUtils.isEmpty(session)){
			throw new NotFoundSessionException();
		}
		return session;
	}
}
