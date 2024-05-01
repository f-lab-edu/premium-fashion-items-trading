package com.inturn.pfit.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record ChangeUserInfoRequestDTO(

		@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
		String userPhone,

		String userName,

		String profileName,

		String profileUrl,

		String alarmYn
){

}
