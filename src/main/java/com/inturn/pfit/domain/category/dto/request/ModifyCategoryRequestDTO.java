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
		Integer categorySort
) {
	public Category modifyCategory(Category category) {
		//맞는지 검증은 별도로 ?
		return Category.builder()
				.categoryId(categoryId)
				.categoryName(categoryName)
				.categorySort(categorySort)
				.build();
	}
}
