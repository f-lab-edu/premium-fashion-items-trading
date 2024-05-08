package com.inturn.pfit.domain.category.dto.response;


import com.inturn.pfit.domain.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryResponseDTO(
		Integer categoryId,
		String categoryName,
		Integer categoryOrder,
		LocalDateTime createdDt,
		LocalDateTime updatedDt
) {
	public static CategoryResponseDTO from(Category entity) {
		return CategoryResponseDTO.builder()
				.categoryId(entity.getCategoryId())
				.categoryName(entity.getCategoryName())
				.categoryOrder(entity.getCategoryOrder())
				.createdDt(entity.getCreatedDt())
				.updatedDt(entity.getUpdatedDt())
				.build();
	}
}
