package com.inturn.pfit.global.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
@NoArgsConstructor
public class CommonTimeDTO {
	@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	private LocalDateTime createdAt;

	@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	private LocalDateTime updatedAt;
}
