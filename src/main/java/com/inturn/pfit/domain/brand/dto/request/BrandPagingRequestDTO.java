package com.inturn.pfit.domain.brand.dto.request;

import lombok.Builder;

@Builder
public record BrandPagingRequestDTO(
		Integer brandId,
		String brandName
) {

}