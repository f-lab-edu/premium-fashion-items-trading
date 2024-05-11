package com.inturn.pfit.domain.brand.dto.request;

import com.inturn.pfit.domain.brand.entity.Brand;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ModifyBrandRequestDTO(
		@NotNull
		Integer brandId,
		@NotEmpty
		@Length(max = 255)
		String brandName
) {
	public Brand modifyBrand(Brand brand) {
		return brand.toBuilder()
				.brandName(brandName)
				.build();
	}
}
