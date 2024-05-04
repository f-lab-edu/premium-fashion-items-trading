package com.inturn.pfit.domain.category.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeleteCategoryRequestDTO(
		@NotNull
		Integer categoryId
) {
}
