package com.inturn.pfit.domain.size.dto.request;


import com.inturn.pfit.domain.size.entity.Size;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ModifySizeRequestDTO(
		@NotNull
		Integer sizeId,
		@NotEmpty
		@Length(max = 255)
		String sizeName
) {
	public Size modifySize(Size Size) {
		return Size.toBuilder()
				.sizeName(sizeName)
				.build();
	}
}
