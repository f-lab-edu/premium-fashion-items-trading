package com.inturn.pfit.domain.size.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.domain.size.entity.Size;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SizeResponseDTO(
		Integer sizeId,
		String sizeName,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime createdAt,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime updatedAt
) {
	public static SizeResponseDTO from(Size entity) {
		return SizeResponseDTO.builder()
				.sizeId(entity.getSizeId())
				.sizeName(entity.getSizeName())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}
