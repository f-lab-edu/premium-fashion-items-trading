package com.inturn.pfit.domain.size.dto.request;


import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.dto.request.ModifySizeTypeRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

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
		@Valid
		@Size(min = 1)
		@NotNull
		List<ModifySizeTypeRequestDTO> sizeTypeList
) {
	public SizeEntity convertSize(SizeEntity size) {
		return size.toBuilder()
				.sizeName(sizeName())
				.categoryId(categoryId())
				.build();
	}
}
