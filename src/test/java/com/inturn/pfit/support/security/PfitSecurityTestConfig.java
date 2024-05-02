package com.inturn.pfit.support.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.global.config.security.SecurityConfig;
import com.inturn.pfit.global.config.security.handler.PfitAccessDeniedHandler;
import com.inturn.pfit.global.config.security.handler.PfitAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class PfitSecurityTestConfig {
	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		final PfitAccessDeniedHandler pfitAccessDeniedHandler = new PfitAccessDeniedHandler(objectMapper);
		final PfitAuthenticationEntryPoint pfitAuthenticationEntryPoint = new PfitAuthenticationEntryPoint(objectMapper);

		return new SecurityConfig(
				pfitAccessDeniedHandler,
				pfitAuthenticationEntryPoint).filterChain(http);
	}
}
