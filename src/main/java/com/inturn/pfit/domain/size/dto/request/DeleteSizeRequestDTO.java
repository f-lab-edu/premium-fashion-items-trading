package com.inturn.pfit.domain.size.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record DeleteSizeRequestDTO(
		@NotNull
		Integer sizeId
) {
}
