package com.inturn.pfit.domain.item.dto.response;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import lombok.Builder;

@Builder
public record CreateItemResponseDTO(
		Long itemId
) {

	public static CreateItemResponseDTO from(ItemEntity entity) {
		return CreateItemResponseDTO.builder().itemId(entity.getItemId()).build();
	}
}
