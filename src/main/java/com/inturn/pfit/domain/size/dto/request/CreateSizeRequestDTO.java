package com.inturn.pfit.domain.size.dto.request;

import com.inturn.pfit.domain.size.entity.SizeEntity;
import com.inturn.pfit.domain.sizetype.dto.request.CreateSizeTypeRequestDTO;
import com.inturn.pfit.domain.sizetype.entity.SizeTypeEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CreateSizeRequestDTO(

		@NotNull
		Integer categoryId,

		@NotEmpty
		@Length(max = 255)
		String sizeName,

		@Valid
		@Size(min = 1)
		@NotNull
		List<CreateSizeTypeRequestDTO> sizeTypeList

) {

	public SizeEntity convertSize() {
		return SizeEntity.builder()
				.categoryId(categoryId())
				.sizeName(sizeName())
				.build();
	}

	public List<SizeTypeEntity> getCreateSizeTypeList(Integer sizeId) {
		return sizeTypeList().stream().map(o -> o.createSizeType(sizeId)).collect(Collectors.toList());
	}

	//sizeTypeList의 sizeTypeOrder 중복 여부 확인
	public boolean isDuplicateSizeTypeOrder() {

		//TODO - 일반적인 경우는 해당 로직에 들어오지 않는데. 통합테스트 시에 들어오면서 에러가 발생하기에 우선 처리.
		if(CollectionUtils.isEmpty(sizeTypeList)){
			return true;
		}

		return !sizeTypeList.stream().map(CreateSizeTypeRequestDTO::sizeTypeOrder)
				.allMatch(new HashSet<>()::add);
	}

}
