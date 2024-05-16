package com.inturn.pfit.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.auth.dto.LoginRequestDTO;
import com.inturn.pfit.domain.auth.service.AuthService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.support.annotation.PfitSecurityConfigTest;
import com.inturn.pfit.support.vo.TestTypeConsts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@PfitSecurityConfigTest(AuthV1Controller.class)
@Tag(TestTypeConsts.UNIT_TEST)
class AuthV1ControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	AuthService authService;

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("로그인(login) - 성공")
	void login_Success() throws Exception{
		//given
		var req = LoginRequestDTO.builder()
				.email("abc@naver.com")
				.password("Thsejrcks1!")
				.build();

		when(authService.login(req)).thenReturn(CommonResponseDTO.ok());

		//when
		ResultActions actions = mockMvc.perform(post("/v1/login")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
//				.andExpect(handler().methodName("login"))
				.andDo(print());
	}

	@Test
	@DisplayName("로그인(login) - Fail : 파라미터 Validator")
	void login_Fail_ParameterValidator() throws Exception{
		//given
		var req = LoginRequestDTO.builder()
				.email("String is not email")
				.build();

		when(authService.login(req)).thenReturn(CommonResponseDTO.ok());

		//when
		ResultActions actions = mockMvc.perform(post("/v1/login")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
//				.andExpect(handler().methodName("login"))
				.andDo(print());
	}

	@Test
	@DisplayName("로그아웃(logout) - 성공")
	void logout_Success() throws Exception{
		//given & when
		ResultActions actions = mockMvc.perform(post("/v1/logout"));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
//				.andExpect(handler().methodName("logout"))
				.andDo(print());
	}

}