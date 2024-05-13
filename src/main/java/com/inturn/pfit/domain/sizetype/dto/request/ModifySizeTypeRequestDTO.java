package com.inturn.pfit.domain.sizetype.dto.request;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.exception.NotFoundSizeTypeException;
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

	public SizeTypeEntity modifySizeType(SizeEntity size) {
		//해당 CREATE, DELETE면 새로 만들어서 처리
		return CUDMode.UPDATE.equals(cudMode()) ?
				//update 이면 toBuilder
				size.getSizeTypeList().stream().filter(o -> sizeTypeId.equals(o.getSizeTypeId())).findFirst().orElseThrow(() -> new NotFoundSizeTypeException())
						.toBuilder()
						.sizeTypeName(sizeTypeName())
						.sizeTypeOrder(sizeTypeOrder())
						.sizeId(size.getSizeId())
						.build()
				:
				SizeTypeEntity.builder()
						.sizeId(size.getSizeId())
						.sizeTypeId(sizeTypeId())
						.sizeTypeName(sizeTypeName())
						.sizeTypeOrder(sizeTypeOrder())
						.build();
	}
}
