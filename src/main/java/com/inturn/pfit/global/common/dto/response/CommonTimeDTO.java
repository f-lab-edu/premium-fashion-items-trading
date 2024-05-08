package com.inturn.pfit.global.common.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommonTimeDTO {
	@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	private LocalDateTime createdDt;

	@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
	private LocalDateTime updatedDt;
}
