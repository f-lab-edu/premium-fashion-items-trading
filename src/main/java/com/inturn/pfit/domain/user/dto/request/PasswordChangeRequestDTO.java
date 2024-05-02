package com.inturn.pfit.domain.user.dto.request;

import com.inturn.pfit.global.support.annotation.Password;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

@Builder
public record PasswordChangeRequestDTO(

		@Password
		@NotEmpty
		String password,

		@Password
		@NotEmpty
		String confirmPassword
) {
	public Boolean isCorrectPassword() {
		return StringUtils.equals(password, confirmPassword);
	}
}