package com.inturn.pfit.domain.category.dto.response;

import lombok.Builder;

@Builder
public record CategoryPagingResponseDTO (
		Integer categoryId,
		String categoryName,
		Integer categorySort
) {

}
