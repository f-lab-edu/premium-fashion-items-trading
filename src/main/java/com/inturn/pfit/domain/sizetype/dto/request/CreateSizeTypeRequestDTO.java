package com.inturn.pfit.domain.sizetype.dto.request;

import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateSizeTypeRequestDTO(

		@NotEmpty
		String sizeTypeName,
		@NotNull
		Integer sizeTypeOrder
) {

	public SizeTypeEntity createSizeType(Integer sizeId) {
		return SizeTypeEntity.builder()
				.sizeId(sizeId)
				.sizeTypeName(sizeTypeName())
				.sizeTypeOrder(sizeTypeOrder())
				.build();
	}
}
