package com.inturn.pfit.domain.size.dto.request;

import lombok.Builder;

@Builder
public record SizePagingRequestDTO(
		Integer sizeId,
		String sizeName,
		Integer categoryId
) {

}