package com.inturn.pfit.domain.sizetype.dto.request;

import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.global.common.vo.CUDMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ModifySizeTypeRequestDTO(

		Integer sizeTypeId,
		@NotEmpty
		String sizeTypeName,
		@NotNull
		Integer sizeTypeOrder,

		@NotEmpty
		CUDMode cudMode
) {

	public SizeTypeEntity modifySizeType(Integer sizeId) {
		return SizeTypeEntity.builder()
				.sizeId(sizeId)
				.sizeTypeId(sizeTypeId())
				.sizeTypeName(sizeTypeName())
				.sizeTypeOrder(sizeTypeOrder())
				.build();
	}
}
