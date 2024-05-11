package com.inturn.pfit.domain.brand.dto.response;

import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BrandPagingResponseDTO extends CommonTimeDTO {

	private Integer brandId;
	private String brandName;

}
