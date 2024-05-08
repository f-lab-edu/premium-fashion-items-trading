package com.inturn.pfit.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.domain.user.entity.UserEntity;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDTO(

		Long userId,

		String email,

		String password,

		String userPhone,

		String userName,

		String profileName,

		String profileUrl,

		Long userPoint,

		String alarmYn,

		String roleCode,

		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime createdDt,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime updatedDt
) {


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
				.createdDt(entity.getCreatedDt())
				.updatedDt(entity.getUpdatedDt())
				.build();
	}
}
