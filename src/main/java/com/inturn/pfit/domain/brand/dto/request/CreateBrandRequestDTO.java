package com.inturn.pfit.domain.brand.dto.request;

import com.inturn.pfit.domain.brand.entity.Brand;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record CreateBrandRequestDTO(
		@NotEmpty
		@Length(max = 255)
		String brandName

) {

	public Brand createBrand() {
		return Brand.builder()
				.brandName(brandName())
				.build();
	}

}
