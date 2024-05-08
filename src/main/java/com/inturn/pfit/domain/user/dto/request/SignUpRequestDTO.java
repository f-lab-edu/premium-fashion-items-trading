package com.inturn.pfit.domain.user.dto.request;


import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.global.support.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;


@Builder
public record SignUpRequestDTO (

		@NotEmpty
		@Email
		String email,

		@NotEmpty
		@Password
		String password,

		@NotEmpty
		@Password
		String confirmPassword,
		@NotEmpty
		@Length(max = 1)
		String alarmYn,

		//roleCode는 전달받아 등록하도록 구성
		//우선 화면에서 전달을 받는 방식으로 구현
		@NotEmpty
		String roleCode
) {
	public Boolean isCorrectPassword() {
		return StringUtils.equals(password, confirmPassword);
	}

	public UserEntity signUpUser(PasswordEncoder encoder) {
		//등록시 사용자명, 프로필명은 random으로 등록
		String name = RandomStringUtils.randomAlphanumeric(10);
		return UserEntity.builder()
				.email(email)
				.password(encoder.encode(password))
				.alarmYn(alarmYn)
				.roleCode(roleCode)
				.userPoint(0l)
				.userName(name)
				.profileName(name)
				.build();
	}
}
