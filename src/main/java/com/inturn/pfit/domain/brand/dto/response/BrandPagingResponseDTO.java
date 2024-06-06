package com.inturn.pfit.domain.brand.dto.response;

import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BrandPagingResponseDTO extends CommonTimeDTO {

	private Integer brandId;
	private String brandName;

}

