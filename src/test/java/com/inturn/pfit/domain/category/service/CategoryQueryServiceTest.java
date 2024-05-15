package com.inturn.pfit.domain.category.service;

import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.exception.ExistCategoryOrderException;
import com.inturn.pfit.domain.category.exception.NotFoundCategoryException;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CategoryQueryServiceTest {

	@InjectMocks
	CategoryQueryService categoryQueryService;

	@Mock
	CategoryRepository categoryRepository;

	String categoryName = "신발";
	Integer categoryOrder = 1;
	Integer categoryId = 1;

	Category category;

	@BeforeEach
	void testStart() {
		category = Category.builder()
				.categoryId(categoryId)
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();
	}

	@Test
	@DisplayName("카테고리 조회 성공")
	void getCategoryById_Success(){

		//given
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		//when
		Category result = categoryQueryService.getCategoryById(categoryId);

		//then
		assertEquals(result.getCategoryId(), categoryId);
	}

	@Test
	@DisplayName("카테고리 데이터가 존재하지 않아 카테고리 조회 실패")
	void getCategoryById_Fail_NotFoundCategory(){

		//given
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		//when
		final NotFoundCategoryException result =  assertThrows(NotFoundCategoryException.class, () -> categoryQueryService.getCategoryById(categoryId));

		//then
		assertEquals(result.getMessage(), CategoryErrorCode.NOT_FOUND_CATEGORY_EXCEPTION.getErrorMessage());
	}

	@Test
	@DisplayName("카테고리 순번으로 카테고리 조회 시 데이터가 없기에 성공")
	void validateExistCategoryByOrder_Success(){

		//given
		when(categoryRepository.findByCategoryOrder(categoryOrder)).thenReturn(Optional.empty());

		//when
		categoryQueryService.validateExistCategoryByOrder(categoryOrder);

		//then
		//verify
		verify(categoryRepository, times(1)).findByCategoryOrder(categoryOrder);
	}

	@Test
	@DisplayName("카테고리 순번으로 조회 시 카테고리 데이터가 존재하여 실패")
	void validateExistCategoryByOrder_Fail(){

		//given
		when(categoryRepository.findByCategoryOrder(categoryOrder)).thenReturn(Optional.of(category));

		//when
		final ExistCategoryOrderException result = assertThrows(ExistCategoryOrderException.class, () -> categoryQueryService.validateExistCategoryByOrder(categoryOrder));

		//then
		assertEquals(result.getMessage(), CategoryErrorCode.EXIST_CATEGORY_SORT_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryRepository, times(1)).findByCategoryOrder(categoryOrder);
	}


}