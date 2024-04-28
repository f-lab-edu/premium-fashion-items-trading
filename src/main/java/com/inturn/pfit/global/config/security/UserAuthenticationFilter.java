package com.inturn.pfit.global.config.security;

import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.define.SessionConst;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

public class UserAuthenticationFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		UserSession user = (UserSession) request.getSession().getAttribute(SessionConst.LOGIN_USER);
		if(!Objects.isNull(user)) {
			//TODO 해당 부분 변경.
			//Object principal, Object credentials과 차이 확인
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(null, user.getUsername(), user.getAuthorities()); // 현재 사용자의 인증 정보
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request,response);
	}
}
