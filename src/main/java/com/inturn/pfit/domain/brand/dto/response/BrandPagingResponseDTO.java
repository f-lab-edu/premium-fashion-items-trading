package com.inturn.pfit.domain.brand.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BrandPagingResponseDTO {

	Integer categoryId;
	String categoryName;
	Integer categorySort;

}
