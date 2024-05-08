package com.inturn.pfit.domain.category.dto.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.global.support.utils.PfitConsts;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CategoryResponseDTO(
		Integer categoryId,
		String categoryName,
		Integer categoryOrder,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
		LocalDateTime createdDt,
		@JsonFormat(pattern = PfitConsts.DateTImeConsts.DATE_TIME)
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
