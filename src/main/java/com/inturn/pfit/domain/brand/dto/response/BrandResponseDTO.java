package com.inturn.pfit.domain.brand.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BrandResponseDTO(
		Integer brandId,
		String brandName,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime createdAt,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime updatedAt
) {
	public static BrandResponseDTO from(Brand entity) {
		return BrandResponseDTO.builder()
				.brandId(entity.getBrandId())
				.brandName(entity.getBrandName())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}
