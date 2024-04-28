package com.inturn.pfit.controller.user.service;

import com.inturn.pfit.domain.user.service.UserCommandService;
import com.inturn.pfit.domain.user.service.UserQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserCommandServiceTest {

	@InjectMocks
	private UserCommandService userCommandService;

	@InjectMocks
	private UserQueryService userQueryService;

	@Test
	@DisplayName("SignUp Success")
	public void signUp_success() {

		//GIVEN
//		SignUpRequestDTO signUpInfo =

		//WHEN

		//THEN

		//VERIFY

	}
}