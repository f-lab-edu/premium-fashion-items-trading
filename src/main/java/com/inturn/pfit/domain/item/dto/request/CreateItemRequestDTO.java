package com.inturn.pfit.domain.item.dto.request;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.itemsize.dto.request.CreateItemSizeRequestDTO;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.domain.itemsize.exception.DuplicateItemSizeOrderException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CreateItemRequestDTO(

		@NotEmpty
		String itemName,
		String itemComment,
		String modelNo,
		String gender,
		String imgUrl,
		@NotNull
		Integer categoryId,

		@NotNull
		Integer brandId,

		@NotNull
		Long retailPrice,

		@NotEmpty
		@Length(max = 1)
		String displayYn,

		LocalDate releaseDate,

		@Valid
		@Size(min = 1)
		@NotNull
		List<CreateItemSizeRequestDTO> itemSizeList
) {

	public ItemEntity convertItem() {
		return ItemEntity.builder()
				.itemName(itemName())
				.itemComment(itemComment())
				.modelNo(modelNo())
				.gender(gender())
				.imgUrl(imgUrl())
				.categoryId(categoryId())
				.brandId(brandId())
				.retailPrice(retailPrice())
				.displayYn(displayYn())
				.releaseDate(releaseDate())
				.build();
	}

	public List<ItemSizeEntity> convertItemSizeList(Long itemId) {
		return itemSizeList().stream().map(o -> o.convertItemSize(itemId)).collect(Collectors.toList());
	}

	public void validateDuplicateItemSizeOrder() {
		if(!itemSizeList.stream().map(CreateItemSizeRequestDTO::itemSizeOrder)
				.allMatch(new HashSet<>()::add)) {
			throw new DuplicateItemSizeOrderException();
		}
	}

}
