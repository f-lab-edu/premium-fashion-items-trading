package com.inturn.pfit.support;

import com.inturn.pfit.domain.user.entity.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessionTestUtils {

	static String email = "abc@naver.com";
	static String password = "@Abc.com1!";
	static String userName = "abc";
	static String userPhone = "01012345678";
	static Long userId = 1L;

	public static UserEntity getUserEntity(String roleCode) {
		return UserEntity.builder()
				.userId(userId)
				.userName(userName)
				.userPhone(userPhone)
				.email(email)
				.password(password)
				.roleCode(roleCode)
				.build();
	}
}
