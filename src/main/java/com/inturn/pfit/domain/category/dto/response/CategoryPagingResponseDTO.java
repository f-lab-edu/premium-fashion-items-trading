package com.inturn.pfit.domain.category.dto.response;

import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPagingResponseDTO extends CommonTimeDTO {
	private Integer categoryId;
	private String categoryName;
	private Integer categoryOrder;

}
