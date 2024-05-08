package com.inturn.pfit.domain.category.dto.request;

import com.inturn.pfit.domain.category.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateCategoryRequestDTO(

		@NotEmpty
		@Length(max = 255)
		String categoryName,

		@NotNull
		Integer categoryOrder

) {

	public Category createCategory() {
		return Category.builder()
				.categoryName(categoryName())
				.categoryOrder(categoryOrder())
				.build();
	}

}
