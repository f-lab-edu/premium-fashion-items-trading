package com.inturn.pfit.domain.category.dto.response;


import com.inturn.pfit.domain.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryResponseDTO(
		Integer categoryId,
		String categoryName,
		Integer categorySort,
		LocalDateTime createdDt,
		LocalDateTime updatedDt
) {
	public static CategoryResponseDTO from(Category entity) {
		return CategoryResponseDTO.builder()
				.categoryId(entity.getCategoryId())
				.categoryName(entity.getCategoryName())
				.categorySort(entity.getCategorySort())
				.createdDt(entity.getCreatedDt())
				.updatedDt(entity.getUpdatedDt())
				.build();
	}
}
