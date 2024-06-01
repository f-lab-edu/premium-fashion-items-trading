package com.inturn.pfit.domain.item.dto.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record ItemPagingRequestDTO(
		Long itemId,

		String itemName,

		String modelNo,
		String gender,
		String imgUrl,

		Integer categoryId,

		Integer brandId,

		Long retailPrice,

		String displayYn,

		LocalDate releaseDate
) {
}
