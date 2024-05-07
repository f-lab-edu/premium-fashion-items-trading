package com.inturn.pfit.domain.brand.dto.response;

import com.inturn.pfit.domain.category.entity.Category;
import lombok.Builder;

@Builder
public record CreateBrandResponseDTO(
		Integer categoryId
) {

	public static CreateBrandResponseDTO from(Category entity) {
		return CreateBrandResponseDTO.builder().categoryId(entity.getCategoryId()).build();
	}
}
