package com.inturn.pfit.domain.size.dto.response;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import lombok.Builder;

@Builder
public record CreateSizeResponseDTO(
		Integer sizeId
) {

	public static CreateSizeResponseDTO from(SizeEntity entity) {
		return CreateSizeResponseDTO.builder().sizeId(entity.getSizeId()).build();
	}
}
