package com.inturn.pfit.domain.brand.dto.request;

import com.inturn.pfit.domain.category.entity.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateBrandRequestDTO(

		@NotEmpty
		@Length(max = 255)
		String categoryName,

		@NotNull
		Integer categorySort

) {

	public Category createCategory() {
		return Category.builder()
				.categoryName(categoryName())
				.categorySort(categorySort())
				.build();
	}

}
