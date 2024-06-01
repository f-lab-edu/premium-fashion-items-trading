package com.inturn.pfit.domain.size.dto.response;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.dto.response.SizeTypeResponseDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SizeResponseDTO extends CommonTimeDTO {

	private Integer sizeId;

	private Integer categoryId;

	private String categoryName;

	private String sizeName;

	private List<SizeTypeResponseDTO> sizeTypeList;

	public static SizeResponseDTO from(SizeEntity entity) {
		return SizeResponseDTO.builder()
				.sizeId(entity.getSizeId())
				.sizeName(entity.getSizeName())
				.categoryId(entity.getCategoryId())
				.categoryName(ObjectUtils.isEmpty(entity.getCategory()) ? null : entity.getCategory().getCategoryName())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.sizeTypeList(SizeTypeResponseDTO.of(entity.getSizeTypeList()))
				.build();
	}

	public static SizeResponseDTO from(SizeEntity entity, List<SizeTypeEntity> sizeTypeEntityList) {
		return SizeResponseDTO.builder()
				.sizeId(entity.getSizeId())
				.sizeName(entity.getSizeName())
				.categoryId(entity.getCategoryId())
				.categoryName(ObjectUtils.isEmpty(entity.getCategory()) ? null : entity.getCategory().getCategoryName())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.sizeTypeList(SizeTypeResponseDTO.of(sizeTypeEntityList))
				.build();
	}
}
