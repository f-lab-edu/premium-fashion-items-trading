package com.inturn.pfit.domain.brand.dto.request;

import com.inturn.pfit.domain.category.entity.Category;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BrandPagingRequestDTO extends Category {

	//아래 부분은 고민할 꺼리가 있네. 정확히 확인해보자.
	public BrandPagingRequestDTO(Integer categoryId, String categoryName, Integer categorySort) {
		super(categoryId, categoryName, categorySort);
	}
}