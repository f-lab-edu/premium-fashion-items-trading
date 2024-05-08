package com.inturn.pfit.domain.category.dto.request;

import com.inturn.pfit.domain.category.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ModifyCategoryRequestDTO(
		@NotNull
		Integer categoryId,
		@NotEmpty
		@Length(max = 255)
		String categoryName,
		@NotNull
		Integer categoryOrder
) {
	public Category modifyCategory(Category category) {
		return category.toBuilder()
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();
	}
}
