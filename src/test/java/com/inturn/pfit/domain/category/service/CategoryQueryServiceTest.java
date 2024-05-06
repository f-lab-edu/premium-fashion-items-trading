package com.inturn.pfit.domain.category.service;

import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.exception.ExistCategorySortException;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
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
	Integer categorySort = 1;
	Integer categoryId = 1;

	Category category;

	@BeforeEach
	void testStart() {
		category = Category.builder()
				.categoryId(categoryId)
				.categoryName(categoryName)
				.categorySort(categorySort)
				.build();
	}

	@Test
	@DisplayName("카테고리 조회(getCategoryById) - 성공")
	void getCategoryById_Success(){

		//given
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

		//when
		Category result = categoryQueryService.getCategoryById(categoryId);

		//then
		assertEquals(result.getCategoryId(), categoryId);
	}

	@Test
	@DisplayName("카테고리 조회(getCategoryById) - 카테고리가 존재하지 않음.")
	void getCategoryById_Fail_NotFoundCategory(){

		//given
		when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

		//when
		final NotFoundException result =  assertThrows(NotFoundException.class, () -> categoryQueryService.getCategoryById(categoryId));

		//then
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_EXCEPTION.getErrorMessage());
	}

	@Test
	@DisplayName("카테고리 유무확인 By Sort(isExistCategoryBySort) - 성공")
	void isExistCategoryBySort_Success(){

		//given
		when(categoryRepository.findByCategorySort(categorySort)).thenReturn(Optional.empty());

		//when
		categoryQueryService.isExistCategoryBySort(categorySort);

		//then
		//verify
		verify(categoryRepository, times(1)).findByCategorySort(categorySort);
	}

	@Test
	@DisplayName("카테고리 유무확인 By Sort(isExistCategoryBySort) - 실패 : 이미 등록된 카테고리")
	void isExistCategoryBySort_Fail(){

		//given
		when(categoryRepository.findByCategorySort(categorySort)).thenReturn(Optional.of(category));

		//when
		final ExistCategorySortException result = assertThrows(ExistCategorySortException.class, () -> categoryQueryService.isExistCategoryBySort(categorySort));

		//then
		assertEquals(result.getMessage(), CategoryErrorCode.EXIST_CATEGORY_SORT_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryRepository, times(1)).findByCategorySort(categorySort);
	}


}