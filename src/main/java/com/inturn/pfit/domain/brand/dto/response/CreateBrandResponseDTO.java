package com.inturn.pfit.domain.brand.dto.response;

import com.inturn.pfit.domain.brand.entity.Brand;
import lombok.Builder;

@Builder
public record CreateBrandResponseDTO(
		Integer brandId
) {

	public static CreateBrandResponseDTO from(Brand entity) {
		return CreateBrandResponseDTO.builder().brandId(entity.getBrandId()).build();
	}
}
