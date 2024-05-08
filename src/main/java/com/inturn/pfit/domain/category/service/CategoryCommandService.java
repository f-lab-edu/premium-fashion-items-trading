package com.inturn.pfit.domain.category.service;


import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.request.DeleteCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.request.ModifyCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryResponseDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryCommandService {

	private final CategoryRepository categoryRepository;

	private final CategoryQueryService categoryQueryService;

	//카테고리 등록
	@Transactional
	public CreateCategoryResponseDTO createCategory(CreateCategoryRequestDTO req) {

		Category category = req.createCategory();

		//이미 해당 categoryOrder가 있을 경우 Exception 발생
		categoryQueryService.validateExistCategoryByOrder(category.getCategoryOrder());

		return CreateCategoryResponseDTO.from(this.save(category));
	}

	@Transactional
	public CategoryResponseDTO modifyCategory(ModifyCategoryRequestDTO req) {

		//해당 카테고리가 존재하는지 조회
		Category category = categoryQueryService.getCategoryById(req.categoryId());

		//이미 해당 categoryOrder가 있을 경우 Exception 발생
		categoryQueryService.validateExistCategoryByOrder(req.categoryOrder());

		Category modCategory = req.modifyCategory(category);

		return CategoryResponseDTO.from(this.save(modCategory));
	}

	@Transactional
	public CommonResponseDTO deleteCategory(DeleteCategoryRequestDTO req) {
		//카테고리 ID로 조회하고 삭제 처리
		categoryRepository.delete(categoryQueryService.getCategoryById(req.categoryId()));
		return CommonResponseDTO.ok();
	}

	@Transactional
	public Category save(Category entity) {
		return categoryRepository.save(entity);
	}

}
