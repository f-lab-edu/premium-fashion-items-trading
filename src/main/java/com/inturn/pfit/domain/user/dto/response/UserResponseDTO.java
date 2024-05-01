package com.inturn.pfit.domain.user.dto.response;

import com.inturn.pfit.domain.user.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserResponseDTO {

	private Long userId;

	private String email;

	private String password;

	private String userPhone;

	private String userName;

	private String profileName;

	private String profileUrl;

	private Long userPoint;

	private String alarmYn;

	private String roleCode;

	public static UserResponseDTO from(UserEntity entity) {
		return UserResponseDTO.builder()
				.userId(entity.getUserId())
				.email(entity.getEmail())
				.password(entity.getPassword())
				.userName(entity.getUserName())
				.userPhone(entity.getUserPhone())
				.profileName(entity.getProfileName())
				.profileUrl(entity.getProfileUrl())
				.userPoint(entity.getUserPoint())
				.alarmYn(entity.getAlarmYn())
				.roleCode(entity.getRoleCode())
				.build();
	}
}
