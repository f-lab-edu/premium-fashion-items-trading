package com.inturn.pfit.domain.user.dto.request;


import com.inturn.pfit.global.support.annotation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;


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
	//TODO 해당 부분은 entity쪽에 들어가는게 맞음.
	public Boolean isCorrectPassword() {
		return StringUtils.equals(password, confirmPassword);
	}
}
