package com.inturn.pfit.domain.category.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPagingResponseDTO {


	Integer categoryId;
	String categoryName;
	Integer categorySort;


}
