package com.inturn.pfit.domain.user.service;

import com.inturn.pfit.domain.user.dto.request.ChangeUserInfoRequestDTO;
import com.inturn.pfit.domain.user.dto.request.PasswordChangeRequestDTO;
import com.inturn.pfit.domain.user.dto.request.SignUpRequestDTO;
import com.inturn.pfit.domain.user.dto.response.UserResponseDTO;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.domain.user.exception.ExistUserException;
import com.inturn.pfit.domain.user.exception.PasswordMismatchException;
import com.inturn.pfit.domain.user.repository.UserRepository;
import com.inturn.pfit.domain.user.vo.UserErrorCode;
import com.inturn.pfit.domain.userrole.entity.UserRole;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.common.exception.NotFoundSessionException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import com.inturn.pfit.global.config.security.service.UserSession;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import com.inturn.pfit.global.config.security.vo.SessionConsts;
import com.inturn.pfit.global.support.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class UserCommandServiceTest {

	@InjectMocks
	UserCommandService userCommandService;

	@Mock
	UserRepository userRepository;

	@Mock
	UserQueryService userQueryService;

	@Spy
	BCryptPasswordEncoder passwordEncoder;

	static String email;
	static String password;
	static String userName;
	static String userPhone;
	static Long userId;

	@BeforeAll
	static void testStart() {
		email = "abc@naver.com";
		password = "@Abc.com!";
		userName = "abc";
		userPhone = "01012345678";
		userId = 1l;
	}

	//DisplayName 명칭 규칙
	//(한글 기능명)(method 명) - (성공 / 실패) : (사유)
	//ex ) 사용자 등록(signUp) - 성공
	//ex ) 사용자 등록(signUp) - 실패 : 기 등록 사용자
	
	//test method 명 규칙
	//(method명)_(Success/Fail)_(사유)
	//ex) signUp_Success
	//es) signUp_Fail_ExistUser
	@Test
	@DisplayName("사용자 등록(signUp) - 성공")
	void signUp_Success() {

		//given
		SignUpRequestDTO req = createSignUpRequestDTO();
		UserRole role = createUserRole();
		UserEntity userParam = UserEntity.signUpUser(req.email(), req.password(), req.alarmYn(), role.getRoleCode(), passwordEncoder);

		when(userRepository.findByEmail(req.email())).thenReturn(Optional.empty());
		when(userRepository.save(any(UserEntity.class))).thenReturn(userParam);

		//when
		UserEntity result = userCommandService.signUp(req, role);

		//then
		assertEquals(result.getEmail(), userParam.getEmail());
		assertEquals(passwordEncoder.matches(req.password(), result.getPassword()), true);
		assertEquals(result.getRoleCode(), userParam.getRoleCode());

		//verify
		verify(userRepository, times(1)).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("사용자 등록(signUp) - 실패 : 기 등록 이메일")
	void signUp_Fail_ExistEmail() {

		//given
		SignUpRequestDTO req = createSignUpRequestDTO();
		UserRole role = createUserRole();
		UserEntity userParam = UserEntity.signUpUser(req.email(), req.password(), req.alarmYn(), role.getRoleCode(), passwordEncoder);

		when(userRepository.findByEmail(req.email())).thenReturn(Optional.of(userParam));

		//when
		final ExistUserException result = assertThrows(ExistUserException.class, () -> userCommandService.signUp(req, role));

		//then
		assertNotNull(result);
		assertEquals(result.getMessage(), UserErrorCode.EXIST_USER_EXCEPTION.getError().getDefaultErrorMessage());
		//verify
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("사용자 등록(signUp) - 실패 : 패스워드 불일치")
	void signUp_Fail_IncorrectPassword() {

		//given
		//password를 반복처리하여 불일치하도록 만듬.
		SignUpRequestDTO req = createSignUpRequestDTO(password.repeat(2));
		UserRole role = createUserRole();

		when(userRepository.findByEmail(req.email())).thenReturn(Optional.empty());

		//when
		final PasswordMismatchException result = assertThrows(PasswordMismatchException.class, () -> userCommandService.signUp(req, role));

		//then
		assertNotNull(result);
		assertEquals(result.getMessage(), UserErrorCode.PASSWORD_MISMATCH_EXCEPTION.getError().getDefaultErrorMessage());
		//verify
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}

	private SignUpRequestDTO createSignUpRequestDTO() {
		return createSignUpRequestDTO(StringUtils.EMPTY);
	}

	private SignUpRequestDTO createSignUpRequestDTO(String notCorrectPassword) {
		return SignUpRequestDTO.builder()
				.email(email)
				.password(password)
				//파라미터가 없을 경우 기존 password와 같이 설정
				//파라미터가 있을 경우 일치하지 않는 password 설정
				.confirmPassword(StringUtils.isEmpty(notCorrectPassword) ? password : notCorrectPassword)
				.alarmYn("Y")
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}

	private UserRole createUserRole() {
		return UserRole.builder().roleCode(RoleConsts.ROLE_USER).build();
	}

	@Test
	@DisplayName("사용자 편집(changeUserInfo) - 성공")
	void changeUserInfo_Success() {
		//given
		var req = createChangeUserInfoRequestDTO();

		//session 설정
		//기 등록 사용자 정보
		var user = getUserEntity();
		setMockSession(new UserSession(user));

		//변경 사용자 정보
		UserEntity changeUser = getUserEntity();
		changeUser.changeUserInfo(req);

		when(userRepository.findById(SessionUtils.getUserSession().getUserId())).thenReturn(Optional.of(user));
		when(userRepository.save(any(UserEntity.class))).thenReturn(changeUser);

		//when
		UserResponseDTO res = userCommandService.changeUserInfo(req);

		//then
		assertEquals(res.getUserName(), req.userName());
		assertEquals(res.getUserPhone(), req.userPhone());
		assertEquals(res.getUserId(), user.getUserId());

		//user
		verify(userRepository, times(1)).save(user);
	}

	@Test
	@DisplayName("사용자 편집(changeUserInfo) - 실패 : Session 정보 없음")
	void changeUserInfo_Fail_NotExsitSession() {
		//given
		var req = createChangeUserInfoRequestDTO();

		setMockSession(null);

		//when & then
		final NotFoundSessionException result = assertThrows(NotFoundSessionException.class, () -> userCommandService.changeUserInfo(req));
		assertNotNull(result);
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_SESSION_EXCEPTION.getError().getDefaultErrorMessage());

		//verify
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}


	@Test
	@DisplayName("사용자 편집(changeUserInfo) - 실패 : 사용자 정보 없음")
	void changeUserInfo_Fail_NotExistUser() {
		//given
		var req = createChangeUserInfoRequestDTO();

		//session 설정
		setMockSession(new UserSession(UserEntity.builder().roleCode(RoleConsts.ROLE_USER)
				.email(email)
				.password(password)
				.userId(userId)
				.build()
		));
		when(userRepository.findById(SessionUtils.getUserSession().getUserId())).thenReturn(Optional.empty());

		//when & then
		final NotFoundException result = assertThrows(NotFoundException.class, () -> userCommandService.changeUserInfo(req));
		assertNotNull(result);
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_EXCEPTION.getError().getDefaultErrorMessage());

		//verify
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}

	private ChangeUserInfoRequestDTO createChangeUserInfoRequestDTO() {
		return ChangeUserInfoRequestDTO.builder()
				.userName(userName.repeat(2))
				.userPhone(userPhone.repeat(2))
				.build();
	}

	private void setMockSession(UserSession userSession) {
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConsts.LOGIN_USER, userSession);

		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setSession(session);
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
	}

	@Test
	@DisplayName("사용자 비밀번호 변경(passwordChange) - 성공")
	void passwordChange_Success() {

		//given
		var req = PasswordChangeRequestDTO.builder().password(password).confirmPassword(password).build();

		var user = getUserEntity();
		//session 설정
		setMockSession(new UserSession(user));

		var changeUser = getUserEntity();
		changeUser.changePassword(req.password(), passwordEncoder);

		when(userRepository.findById(SessionUtils.getUserSession().getUserId())).thenReturn(Optional.of(user));
		when(userRepository.save(changeUser)).thenReturn(changeUser);

		//when
		CommonResponseDTO res = userCommandService.passwordChange(req);

		//then
		assertNotEquals(user.getPassword(), changeUser.getPassword());
		assertEquals(res.getSuccess(), true);

		//verify
		verify(userRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("사용자 비밀번호 변경(passwordChange) - 실패 : 사용자 정보 없음")
	void passwordChange_Fail_NotExistUser() {
		//given
		var req = PasswordChangeRequestDTO.builder().password(password).confirmPassword(password).build();

		//session 설정
		setMockSession(new UserSession(getUserEntity()));
		when(userRepository.findById(SessionUtils.getUserSession().getUserId())).thenReturn(Optional.empty());

		//when & then
		final NotFoundException result = assertThrows(NotFoundException.class, () -> userCommandService.passwordChange(req));
		assertNotNull(result);
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_EXCEPTION.getError().getDefaultErrorMessage());

		//verify
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}

	@Test
	@DisplayName("사용자 비밀번호 변경(passwordChange) - 실패 : 비밀번호 불일치")
	void passwordChange_Fail_NotMatchPassword() {
		//given
		var req = PasswordChangeRequestDTO.builder().password(password).confirmPassword(password.repeat(2)).build();

		//session 설정
		setMockSession(new UserSession(getUserEntity()));

		//when & then
		final PasswordMismatchException result = assertThrows(PasswordMismatchException.class, () -> userCommandService.passwordChange(req));
		assertNotNull(result);
		assertEquals(result.getMessage(), UserErrorCode.PASSWORD_MISMATCH_EXCEPTION.getError().getDefaultErrorMessage());

		//verify
		verify(userRepository, times(0)).findById(any());
		verify(userRepository, times(0)).save(any(UserEntity.class));
	}

	private UserEntity getUserEntity() {
		return UserEntity.builder()
				.userId(userId)
				.userName(userName)
				.userPhone(userPhone)
				.email(email)
				.password(passwordEncoder.encode(password))
				.roleCode(RoleConsts.ROLE_USER)
				.build();
	}
}