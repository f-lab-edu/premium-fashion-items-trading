package com.inturn.pfit.domain.brand.dto.response;


import com.inturn.pfit.domain.brand.entity.Brand;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BrandResponseDTO(
		Integer brandId,
		String brandName,
		LocalDateTime createdDt,
		LocalDateTime updatedDt
) {
	public static BrandResponseDTO from(Brand entity) {
		return BrandResponseDTO.builder()
				.brandId(entity.getBrandId())
				.brandName(entity.getBrandName())
				.createdDt(entity.getCreatedDt())
				.updatedDt(entity.getUpdatedDt())
				.build();
	}
}
