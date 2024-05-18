package com.inturn.pfit.domain.item.dto.response;

import com.inturn.pfit.domain.item.entity.ItemEntity;
import com.inturn.pfit.domain.itemsize.dto.response.ItemSizeResponseDTO;
import com.inturn.pfit.domain.itemsize.entity.ItemSizeEntity;
import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.List;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponseDTO extends CommonTimeDTO {

	private Long itemId;

	private String itemName;

	private String itemComment;
	private String modelNo;
	private String gender;
	private String imgUrl;

	private Integer categoryId;
	private String categoryName;

	private Integer brandId;
	private String brandName;

	private Long retailPrice;

	private String displayYn;

	private LocalDate releaseDate;

	private List<ItemSizeResponseDTO> itemSizeList;

	public static ItemResponseDTO from(ItemEntity entity) {
		return fromEntity(entity)
				.toBuilder()
				.itemSizeList(ItemSizeResponseDTO.of(entity.getItemSizeList()))
				.build();
	}

	public static ItemResponseDTO from(ItemEntity entity, List<ItemSizeEntity> itemSizeEntityList) {
		return fromEntity(entity)
				.toBuilder()
				.itemSizeList(ItemSizeResponseDTO.of(itemSizeEntityList))
				.build();
	}

	private static ItemResponseDTO fromEntity(ItemEntity entity) {
		return ItemResponseDTO.builder()
				.itemId(entity.getItemId())
				.itemName(entity.getItemName())
				.itemComment(entity.getItemComment())
				.modelNo(entity.getModelNo())
				.gender(entity.getGender())
				.imgUrl(entity.getImgUrl())
				.categoryId(entity.getCategoryId())
				.categoryName(ObjectUtils.isEmpty(entity.getCategory()) ? null : entity.getCategory().getCategoryName())
				.brandId(entity.getBrandId())
				.brandName(ObjectUtils.isEmpty(entity.getBrand()) ? null : entity.getBrand().getBrandName())
				.retailPrice(entity.getRetailPrice())
				.displayYn(entity.getDisplayYn())
				.releaseDate(entity.getReleaseDate())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}
}
