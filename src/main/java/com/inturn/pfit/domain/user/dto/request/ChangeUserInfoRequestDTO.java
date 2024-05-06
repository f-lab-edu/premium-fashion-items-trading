package com.inturn.pfit.domain.user.dto.request;

import com.inturn.pfit.domain.user.entity.UserEntity;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ChangeUserInfoRequestDTO(

		@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
		String userPhone,

		String userName,

		String profileName,

		String profileUrl,

		String alarmYn
){
	public UserEntity changeUserInfo(UserEntity user) {
		user.changeUserInfo(this);
		return user;
	}
}
