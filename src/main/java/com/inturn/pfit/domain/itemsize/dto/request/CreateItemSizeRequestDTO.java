package com.inturn.pfit.domain.itemsize.dto.request;

import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateItemSizeRequestDTO(

		@NotEmpty
		String itemSizeName,

		@NotNull
		Integer itemSizeOrder
) {

	public ItemSizeEntity convertItemSize(Long itemId) {
		return ItemSizeEntity.builder()
				.itemId(itemId)
				.itemSizeName(itemSizeName())
				.itemSizeOrder(itemSizeOrder())
				.build();
	}
}
