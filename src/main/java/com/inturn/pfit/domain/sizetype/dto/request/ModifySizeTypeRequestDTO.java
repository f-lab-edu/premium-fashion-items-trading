package com.inturn.pfit.domain.sizetype.dto.request;

import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import com.inturn.pfit.domain.sizetype.exception.SizeTypeNotFoundException;
import com.inturn.pfit.global.common.vo.CUDRequestCommand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ModifySizeTypeRequestDTO(

		Integer sizeTypeId,
		@NotEmpty
		String sizeTypeName,
		@NotNull
		Integer sizeTypeOrder,
		@NotNull
		CUDRequestCommand cudRequestCommand
) {

	public SizeTypeEntity modifySizeType(Integer sizeId, List<SizeTypeEntity> sizeTypeList) {
		//해당 CREATE, DELETE면 새로 만들어서 처리
		return CUDRequestCommand.UPDATE.equals(cudRequestCommand()) ?
				//update 이면 toBuilder
				sizeTypeList.stream().filter(o -> sizeTypeId.equals(o.getSizeTypeId())).findFirst().orElseThrow(() -> new SizeTypeNotFoundException())
						.toBuilder()
						.sizeTypeName(sizeTypeName())
						.sizeTypeOrder(sizeTypeOrder())
						.sizeId(sizeId)
						.build()
				:
				SizeTypeEntity.builder()
						.sizeId(sizeId)
						.sizeTypeId(sizeTypeId())
						.sizeTypeName(sizeTypeName())
						.sizeTypeOrder(sizeTypeOrder())
						.build();
	}
}
