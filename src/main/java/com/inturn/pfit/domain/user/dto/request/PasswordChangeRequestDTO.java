package com.inturn.pfit.domain.user.dto.request;

import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.global.support.annotation.Password;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public record PasswordChangeRequestDTO(

		@Password
		@NotEmpty
		String password,

		@Password
		@NotEmpty
		String confirmPassword
) {
	public boolean isCorrectPassword() {
		return StringUtils.equals(password, confirmPassword);
	}

	public UserEntity changePassword(UserEntity user, PasswordEncoder encoder) {
		user.changePassword(password, encoder);
		return user;
	}
}
