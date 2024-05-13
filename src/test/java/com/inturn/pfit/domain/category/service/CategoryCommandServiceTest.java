package com.inturn.pfit.domain.category.service;

import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.request.ModifyCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryResponseDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.exception.ExistCategoryOrderException;
import com.inturn.pfit.domain.category.exception.NotFoundCategoryException;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.domain.category.vo.CategoryErrorCode;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CategoryCommandServiceTest {

	@InjectMocks
	CategoryCommandService categoryCommandService;

	@Mock
	CategoryRepository categoryRepository;

	@Mock
	CategoryQueryService categoryQueryService;


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
	@DisplayName("카테고리 등록 성공")
	void createCategory_Success(){
		//given
		CreateCategoryRequestDTO req = CreateCategoryRequestDTO.builder()
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();
		doNothing().when(categoryQueryService).validateExistCategoryByOrder(categoryOrder);
		when(categoryRepository.save(any())).thenReturn(category);

		//when
		CreateCategoryResponseDTO res = categoryCommandService.createCategory(req);

		//then
		assertNotNull(res.categoryId());

		//verify
		verify(categoryRepository, times(1)).save(any());
		verify(categoryQueryService, times(1)).validateExistCategoryByOrder(categoryOrder);
	}

	@Test
	@DisplayName("카테고리 순번이 이미 존재하여 카테고리 등록 실패")
	void createCategory_Fail_ExistCategoryOrder(){
		//given
		CreateCategoryRequestDTO req = CreateCategoryRequestDTO.builder()
				.categoryName(categoryName)
				.categoryOrder(categoryOrder)
				.build();
		doThrow(new ExistCategoryOrderException()).when(categoryQueryService).validateExistCategoryByOrder(categoryOrder);

		//when
		final ExistCategoryOrderException result = assertThrows(ExistCategoryOrderException.class, () -> categoryCommandService.createCategory(req));

		//then
		assertEquals(result.getMessage(), CategoryErrorCode.EXIST_CATEGORY_SORT_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryQueryService, times(1)).validateExistCategoryByOrder(categoryOrder);
	}

	@Test
	@DisplayName("카테고리 편집 성공")
	void modifyCategory_Success() {
		//given
		ModifyCategoryRequestDTO req = ModifyCategoryRequestDTO.builder()
				.categoryId(categoryId)
				.categoryName(categoryName.repeat(2))
				.categoryOrder(categoryOrder + 1)
				.build();
		when(categoryQueryService.getCategoryById(req.categoryId())).thenReturn(category);
		doNothing().when(categoryQueryService).validateExistCategoryByOrder(req.categoryOrder());
		Category modCategory = Category.builder()
				.categoryId(categoryId)
				.categoryName(categoryName.repeat(2))
				.categoryOrder(categoryOrder + 1)
				.build();
		when(categoryRepository.save(any())).thenReturn(modCategory);

		//when
		CategoryResponseDTO res = categoryCommandService.modifyCategory(req);

		//then
		assertEquals(req.categoryId(), res.categoryId());
		assertEquals(req.categoryOrder(), res.categoryOrder());
		assertEquals(req.categoryName(), res.categoryName());

		//verify
		verify(categoryRepository, times(1)).save(any());
		verify(categoryQueryService, times(1)).getCategoryById(req.categoryId());
		verify(categoryQueryService, times(1)).validateExistCategoryByOrder(req.categoryOrder());
	}

	@Test
	@DisplayName("존재하지 않는 카테고리 데이터로 편집하여 편집 실패")
	void modifyCategory_Fail_NotFoundCategory() {
		//given
		ModifyCategoryRequestDTO req = ModifyCategoryRequestDTO.builder()
				.categoryId(categoryId)
				.categoryName(categoryName.repeat(2))
				.categoryOrder(categoryOrder + 1)
				.build();
		when(categoryQueryService.getCategoryById(req.categoryId())).thenThrow(new NotFoundCategoryException());

		//when
		final NotFoundCategoryException result = assertThrows(NotFoundCategoryException.class, () -> categoryCommandService.modifyCategory(req));

		//then
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_EXCEPTION.getErrorMessage());

		//verify
		verify(categoryQueryService, times(1)).getCategoryById(req.categoryId());
		verify(categoryQueryService, times(0)).validateExistCategoryByOrder(req.categoryOrder());
		verify(categoryRepository, times(0)).save(any());
	}

}