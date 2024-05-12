package com.inturn.pfit.domain.sizetype.dto.response;

import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class SizeTypeResponseDTO extends CommonTimeDTO {

	private Integer sizeTypeId;

	private String sizeTypeName;

	private Integer sizeId;

	private Integer sizeTypeOrder;

	public static SizeTypeResponseDTO from(SizeTypeEntity entity) {
		return SizeTypeResponseDTO.builder()
				.sizeTypeId(entity.getSizeTypeId())
				.sizeTypeName(entity.getSizeTypeName())
				.sizeId(entity.getSizeId())
				.sizeTypeOrder(entity.getSizeTypeOrder())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

}
