package com.inturn.pfit.domain.size.dto.request;


import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.dto.request.ModifySizeTypeRequestDTO;
import com.inturn.pfit.global.common.vo.CUDMode;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.List;

@Builder
public record ModifySizeRequestDTO(
		@NotNull
		Integer sizeId,
		@NotNull
		Integer categoryId,
		@NotEmpty
		@Length(max = 255)
		String sizeName,
		@NotEmpty
		List<ModifySizeTypeRequestDTO> sizeTypeList
) {
	public SizeEntity modifySize(SizeEntity size) {
		return size.toBuilder()
				.sizeName(sizeName())
				.categoryId(categoryId())
				.build();
	}

	public boolean isDuplicateSizeTypeOrder() {
		//DELETE가 아닌 데이터 중 중복이 있는지 확인
		return !sizeTypeList.stream().filter(o -> !CUDMode.DELETE.equals(o.cudMode()))
				.map(ModifySizeTypeRequestDTO::sizeTypeOrder).allMatch(new HashSet<>()::add);
	}

}
