package com.inturn.pfit.domain.itemsize.dto.request;

import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.exception.ItemSizeNotFoundException;
import com.inturn.pfit.global.common.vo.CUDRequestCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ModifyItemSizeRequestDTO(

		Long itemSizeId,
		@NotEmpty
		String itemSizeName,
		@NotNull
		Integer itemSizeOrder,
		@NotNull
		CUDRequestCommand cudRequestCommand
) {

	public ItemSizeEntity convertItemSize(Long itemId, List<ItemSizeEntity> itemSizeList) {
		//해당 CREATE, DELETE면 새로 만들어서 처리
		return CUDRequestCommand.UPDATE.equals(cudRequestCommand()) ?
				//update 이면 toBuilder
				itemSizeList.stream().filter(o -> itemSizeId.equals(o.getItemSizeId())).findFirst().orElseThrow(() -> new ItemSizeNotFoundException())
						.toBuilder()
						.itemSizeName(itemSizeName())
						.itemSizeOrder(itemSizeOrder())
						.itemId(itemId)
						.build()
				:
				ItemSizeEntity.builder()
						.itemId(itemId)
						.itemSizeId(itemSizeId())
						.itemSizeName(itemSizeName())
						.itemSizeOrder(itemSizeOrder())
						.build();
	}
}
