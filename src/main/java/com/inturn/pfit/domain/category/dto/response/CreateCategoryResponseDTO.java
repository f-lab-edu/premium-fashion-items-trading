package com.inturn.pfit.domain.category.dto.response;

import com.inturn.pfit.domain.category.entity.Category;
import lombok.Builder;

@Builder
public record CreateCategoryResponseDTO(
		Integer categoryId
) {

	public static CreateCategoryResponseDTO from(Category entity) {
		return CreateCategoryResponseDTO.builder().categoryId(entity.getCategoryId()).build();
	}
}
