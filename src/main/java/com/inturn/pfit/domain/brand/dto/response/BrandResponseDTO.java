package com.inturn.pfit.domain.brand.dto.response;


import com.inturn.pfit.domain.category.entity.Category;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BrandResponseDTO(
		Integer categoryId,
		String categoryName,
		Integer categorySort,
		LocalDateTime createdDt,
		LocalDateTime updatedDt
) {
	public static BrandResponseDTO from(Category entity) {
		return BrandResponseDTO.builder()
				.categoryId(entity.getCategoryId())
				.categoryName(entity.getCategoryName())
				.categorySort(entity.getCategorySort())
				.createdDt(entity.getCreatedDt())
				.updatedDt(entity.getUpdatedDt())
				.build();
	}
}
