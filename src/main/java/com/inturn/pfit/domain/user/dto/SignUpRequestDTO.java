package com.inturn.pfit.domain.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;


public record SignUpRequestDTO (

		@NotEmpty
		@Email
		String email,

		//TODO - password는 ConstraintValidator를 통해 유효성 검증
		@NotEmpty
		String password,
		@NotEmpty
		@Length(max = 1)
		String alarmYn,

		@NotEmpty
		//TODO - roleCode는 전달받을지 아니면 controller를 분리하여 처리할지 추후 결정
		//우선 화면에서 전달을 받는 방식으로 구현
		String roleCode
) {

}
