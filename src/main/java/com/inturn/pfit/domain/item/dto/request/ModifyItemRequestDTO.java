package com.inturn.pfit.domain.item.dto.request;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.itemsize.dto.request.ModifyItemSizeRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ModifyItemRequestDTO(

		@NotNull
		Long itemId,
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
		List<ModifyItemSizeRequestDTO> itemSizeList

) {

	public ItemEntity convertItem(ItemEntity entity) {
		return entity.toBuilder()
				.itemName(itemName())
				.itemComment(StringUtils.isEmpty(this.itemComment()) ? entity.getItemComment() : this.itemComment())
				.modelNo(StringUtils.isEmpty(this.modelNo()) ? entity.getModelNo() : this.modelNo())
				.gender(StringUtils.isEmpty(this.gender()) ? entity.getGender() : this.gender())
				.imgUrl(StringUtils.isEmpty(this.imgUrl()) ? entity.getImgUrl() : this.imgUrl())
				.categoryId(categoryId())
				.brandId(brandId())
				.retailPrice(retailPrice())
				.displayYn(displayYn())
				.releaseDate(ObjectUtils.isEmpty(this.releaseDate()) ? entity.getReleaseDate() : this.releaseDate())
				.build();
	}

}
