package com.inturn.pfit.domain.user.dto.request;

import com.inturn.pfit.domain.user.entity.UserEntity;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

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
		return UserEntity.builder()
				.userId(user.getUserId())
				.email(user.getEmail())
				.password(user.getPassword())
				.userPoint(user.getUserPoint())
				.roleCode(user.getRoleCode())
				//변경 포인트
				.userPhone(StringUtils.isEmpty(this.userPhone()) ? user.getUserPhone() : this.userPhone())
				.userName(StringUtils.isEmpty(this.userName()) ? user.getUserName() : this.userName())
				.profileName(StringUtils.isEmpty(this.profileName()) ? user.getProfileName() : this.profileName())
				.profileUrl(StringUtils.isEmpty(this.profileUrl()) ? user.getProfileUrl() : this.profileUrl())
				.alarmYn(StringUtils.isEmpty(this.alarmYn()) ? user.getAlarmYn() : this.alarmYn())
				////
				.build();
	}
}
