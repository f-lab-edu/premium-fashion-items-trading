package com.inturn.pfit.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inturn.pfit.domain.user.dto.request.ChangeUserInfoRequestDTO;
import com.inturn.pfit.domain.user.dto.request.PasswordChangeRequestDTO;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.SignUpResponseDTO;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.user.service.UserQueryService;
import com.inturn.pfit.domain.user.usecase.SignUpService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.config.security.define.RoleConsts;
import com.inturn.pfit.global.config.security.define.SessionConsts;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.support.annotation.PfitSecurityConfigTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@PfitSecurityConfigTest(UserController.class)
//TODO - EnableGlobalMethodSecurity도 PfitSecurityConfigTest 어노테이션에 추가하고 싶은데 @Secured 관련 에러 발생, 추후 확인
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	UserCommandService userCommandService;

	@MockBean
	SignUpService signUpService;

	@MockBean
	UserQueryService userQueryService;

	ObjectMapper objectMapper = new ObjectMapper();


	static String email;
	static String password;
	static String userName;
	static String userPhone;
	static Long userId;

	@BeforeAll
	static void testStart() {
		email = "abc@naver.com";
		password = "@Abc.com1!";
		userName = "abc";
		userPhone = "01012345678";
		userId = 1l;
	}

	@Test
	@DisplayName("사용자 등록(signUp) - 성공")
	void signUp_Success() throws Exception{

		//given
		var req = createSignUpRequestDTO(password);
		var res = createSignUpResponseDTO();

		when(signUpService.signUp(req)).thenReturn(res);

		//when
		ResultActions actions = mockMvc.perform(post("/user/signUp")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.userId").value(userId))
				.andExpect(handler().methodName("signUp"))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 등록(signUp) - 실패 : 패스워드 정규식과 불일치")
	void signUp_Fail_PasswordIsNotMatchRegexp() throws Exception{
		//given
		var req = createSignUpRequestDTO("This is not Password Type");

		//when
		ResultActions actions = mockMvc.perform(post("/user/signUp")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON)
		);

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(handler().methodName("signUp"))
				.andDo(print());
	}

	private SignUpRequestDTO createSignUpRequestDTO(String password) {
		return SignUpRequestDTO.builder()
				.email(email)
				.password(password)
				.confirmPassword(password)
				.alarmYn("Y")
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	private SignUpResponseDTO createSignUpResponseDTO() {
		return new SignUpResponseDTO(userId);
	}

	@Test
	@DisplayName("사용자 조회(getUser) - 성공")
	@WithMockUser(authorities = RoleConsts.ROLE_USER)
	void getUser_Success() throws Exception{

		var user = getUserEntity();
		//given
		when(userQueryService.getUserBySession()).thenReturn(UserResponseDTO.from(user));

		//when
		ResultActions actions = mockMvc.perform(get("/user")
						.session(setMockSession(new UserSession(user)))
				);

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.userId").value(userId))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 조회(getUser) - 실패 : FORBIDDEN")
	//@Secured 권한이 없는 사용자로 인증
	@WithMockUser(authorities = RoleConsts.ROLE_ADMIN)
	void getUser_Fail_NotAuthorize() throws Exception{
		//given
		//when
		ResultActions actions = mockMvc.perform(get("/user"));

		//then
		actions
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 조회(getUser) - 실패 : AnonymousUser")
	@WithAnonymousUser
	void getUser_Fail_AnonymousUser() throws Exception{
		//given
		//when
		ResultActions actions = mockMvc.perform(get("/user"));

		//then
		actions
				//익명 유저이기에 isUnauthorized
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 편집(changeUserInfo) - 성공")
	@WithMockUser
	void changeUserInfo_success() throws Exception{
		//given
		ChangeUserInfoRequestDTO req = createChangeUserInfoRequestDTO();

		var user = getUserEntity();
		var changeUserInfo = getUserEntity();
		changeUserInfo.changeUserInfo(req.userPhone(), req.userName(), req.profileName(), req.profileUrl(), req.alarmYn());
		when(userCommandService.changeUserInfo(req)).thenReturn(UserResponseDTO.from(changeUserInfo));

		//when
		ResultActions actions = mockMvc.perform(patch("/user/info")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(req))
						.session(setMockSession(new UserSession(user)))
				);

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").exists())
				.andExpect(jsonPath("$.success").value(true))
				//userId가 존재하고
				.andExpect(jsonPath("$.data.userId").exists())
				//userPhone이 변경되었는지 확인
				.andExpect(jsonPath("$.data.userPhone").value(req.userPhone()))
				.andExpect(handler().methodName("changeUserInfo"))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 편집(changeUserInfo) - 실패 : 정규식 일치하지 않음.")
	@WithMockUser
	void changeUserInfo_Fail_NotMatchRegexp() throws Exception{
		//given
		//번호가 아닌 수를 set하여 @Valid Test
		ChangeUserInfoRequestDTO req = ChangeUserInfoRequestDTO.builder().userPhone("Not Number").build();

		//when
		ResultActions actions = mockMvc.perform(patch("/user/info")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))
		);

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(handler().methodName("changeUserInfo"))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 중복 확인(duplicateUser) - 성공")
	void duplicateUser_Success() throws Exception{
		//given
		when(userQueryService.duplicateUser(email)).thenReturn(new CommonResponseDTO());

		//when
		ResultActions actions = mockMvc.perform(get("/user/check/%s".formatted(email)));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(handler().methodName("duplicateUser"))
				.andDo(print());
	}

	@Test
	@DisplayName("사용자 중복 확인(duplicateUser) - 실패 : 파라미터가 이메일 형식이 아님")
	void duplicateUser_Fail_ParamIsNotEmail() throws Exception{

		//given
		String notEmail = "Not Email";
		when(userQueryService.duplicateUser(notEmail)).thenReturn(new CommonResponseDTO());

		//when
		ResultActions actions = mockMvc.perform(get("/user/check/%s".formatted(notEmail)));

		//then
		actions
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.data").doesNotExist())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(handler().methodName("duplicateUser"))
				.andDo(print());
	}

	private MockHttpSession setMockSession(UserSession userSession) {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, userSession);
		return session;
	}

	//TODO - 해당부분은 여러 Test 클래스에서 사용하는데 공통화 시킬 필요가 있을까 ?
	private UserEntity getUserEntity() {
		return UserEntity.builder()
				.userId(userId)
				.userName(userName)
				.userPhone(userPhone)
				.email(email)
				.password(password)
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	private ChangeUserInfoRequestDTO createChangeUserInfoRequestDTO() {
		return ChangeUserInfoRequestDTO.builder()
				.userPhone("01012345678")
				.userName(userName)
				.alarmYn("Y")
				.build();
	}
	
	@Test
	@DisplayName("사용자 패스워드 변경(changePassword) - 성공")
	@WithMockUser
	void changePassword_Success() throws Exception{

		//given
		PasswordChangeRequestDTO req = PasswordChangeRequestDTO.builder()
				.password(password)
				.confirmPassword(password)
				.build();

		when(userCommandService.passwordChange(req)).thenReturn(new CommonResponseDTO());

		//when
		ResultActions actions = mockMvc.perform(patch("/user/password")
				.content(objectMapper.writeValueAsString(req))
				.contentType(MediaType.APPLICATION_JSON));

		//then
		actions
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.message").value(StringUtils.EMPTY));
	}

}