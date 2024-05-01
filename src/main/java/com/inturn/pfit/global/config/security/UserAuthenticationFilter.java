package com.inturn.pfit.global.config.security;

import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.define.SessionConsts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
/*
OncePerRequestFilter를 통해 해당 Filter를 거친 요청이 redirect 또는 별도의 상황에 의해 재호출되지 않도록 한다.
 */
public class UserAuthenticationFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		UserSession user = (UserSession) request.getSession().getAttribute(SessionConsts.LOGIN_USER);
		if(!Objects.isNull(user)) {
			//Object principal, Object credentials과 차이 확인
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities()); // 현재 사용자의 인증 정보
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request,response);
	}
}
