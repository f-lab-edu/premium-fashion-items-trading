package com.inturn.pfit.domain.brand.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeleteBrandRequestDTO(
		@NotNull
		Integer brandId
) {
}
