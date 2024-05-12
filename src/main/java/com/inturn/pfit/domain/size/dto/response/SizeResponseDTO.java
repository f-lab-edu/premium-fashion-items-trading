package com.inturn.pfit.domain.size.dto.response;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.dto.response.SizeTypeResponseDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SizeResponseDTO extends CommonTimeDTO {

	private Integer sizeId;

	private Integer categoryId;

	private String sizeName;

	private List<SizeTypeResponseDTO> sizeTypeList;

	public static SizeResponseDTO from(SizeEntity entity) {
		return SizeResponseDTO.builder()
				.sizeId(entity.getSizeId())
				.sizeName(entity.getSizeName())
				.categoryId(entity.getCategoryId())
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.sizeTypeList(of(entity.getSizeTypeList()))
				.build();
	}

	private static List<SizeTypeResponseDTO> of(List<SizeTypeEntity> entityList) {
		return entityList.stream().map(SizeTypeResponseDTO::from).collect(Collectors.toList());
	}
}
