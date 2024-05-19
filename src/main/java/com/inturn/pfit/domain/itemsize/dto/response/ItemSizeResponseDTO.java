package com.inturn.pfit.domain.itemsize.dto.response;

import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ItemSizeResponseDTO extends CommonTimeDTO {

	private Long itemSizeId;

	private Long itemId;

	private String itemSizeName;

	private Integer itemSizeOrder;

	public static ItemSizeResponseDTO from(ItemSizeEntity entity) {
		return ItemSizeResponseDTO.builder()
				.itemSizeId(entity.getItemSizeId())
				.itemId(entity.getItemId())
				.itemSizeName(entity.getItemSizeName())
				.itemSizeOrder(entity.getItemSizeOrder())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

	public static List<ItemSizeResponseDTO> of(List<ItemSizeEntity> entityList) {
		return entityList.stream().map(ItemSizeResponseDTO::from).collect(Collectors.toList());
	}

}
