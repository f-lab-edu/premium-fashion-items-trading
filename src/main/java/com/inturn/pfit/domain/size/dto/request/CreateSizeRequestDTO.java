package com.inturn.pfit.domain.size.dto.request;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.dto.request.CreateSizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CreateSizeRequestDTO(

		@NotNull
		Integer categoryId,

		@NotEmpty
		@Length(max = 255)
		String sizeName,

		@NotEmpty
		List<CreateSizeTypeRequestDTO> sizeTypeList

) {

	public SizeEntity createSize() {
		return SizeEntity.builder()
				.categoryId(categoryId())
				.sizeName(sizeName())
				.build();
	}

	public List<SizeTypeEntity> getCsreateSizeTypeList(Integer sizeId) {
		return sizeTypeList().stream().map(o -> o.createSizeType(sizeId)).collect(Collectors.toList());
	}

}
