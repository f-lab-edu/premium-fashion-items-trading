package com.inturn.pfit.domain.category.dto.request;

import lombok.Builder;

@Builder
public record CategoryPagingRequestDTO(

		Integer categoryId,

		String categoryName,

		Integer categorySort
) {

}