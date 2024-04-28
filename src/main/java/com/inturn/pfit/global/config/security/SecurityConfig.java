package com.inturn.pfit.global.config.security;

import com.inturn.pfit.global.config.security.handler.PfitAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

	private final AccessDeniedHandler accessDeniedHandler;
	private final AuthenticationEntryPoint authenticationEntryPoint;

	/*
	기존 WebSecurityConfigurerAdapter를 상속 후에 configure 메소드를 오버라이딩 하는 방식은 deprecated. */
	//,
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(r ->
						r.requestMatchers( "/user/signUp", "/login", "/logout", "/error").permitAll()
								.anyRequest().authenticated()
				)
				.csrf(CsrfConfigurer::disable)
				.sessionManagement(sessionManagement ->
						sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
								httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler)
								.authenticationEntryPoint(authenticationEntryPoint)
				)
				.httpBasic(Customizer.withDefaults())
				.addFilterBefore(new UserAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
