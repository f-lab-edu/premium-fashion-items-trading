package com.inturn.pfit.domain.size.dto.request;

import com.inturn.pfit.domain.size.entity.Size;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateSizeRequestDTO(
		@NotEmpty
		@Length(max = 255)
		String sizeName

) {

	public Size createSize() {
		return Size.builder()
				.sizeName(sizeName())
				.build();
	}

}
