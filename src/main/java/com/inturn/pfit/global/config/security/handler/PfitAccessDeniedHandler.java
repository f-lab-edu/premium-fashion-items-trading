package com.inturn.pfit.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class PfitAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper objectMapper;
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(CommonResponseDTO.fail(CommonErrorCode.FORBIDDEN.getError().getDefaultErrorMessage())));
	}
}
