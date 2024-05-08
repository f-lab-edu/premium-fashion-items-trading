package com.inturn.pfit.domain.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPagingResponseDTO {
	private Integer categoryId;
	private String categoryName;
	private Integer categoryOrder;
}
