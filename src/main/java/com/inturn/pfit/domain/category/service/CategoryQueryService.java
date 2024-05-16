package com.inturn.pfit.domain.category.service;

import com.inturn.pfit.domain.category.dto.request.CategoryPagingRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryPagingResponseDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryResponseDTO;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.exception.CategoryNotFoundException;
import com.inturn.pfit.domain.category.exception.ExistCategoryOrderException;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<CategoryPagingResponseDTO> getCategoryPagingList(CategoryPagingRequestDTO req, Pageable page) {
		return categoryRepository.getPagingList(req, page);
	}

	@Transactional(readOnly = true)
	public CategoryResponseDTO getCategory(Integer categoryId) {
		return CategoryResponseDTO.from(getCategoryById(categoryId));
	}

	@Transactional(readOnly = true)
	public Category getCategoryById(Integer categoryId) {
		return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());
	}

	@Transactional(readOnly = true)
	public void validateCategoryById(Integer categoryId) {
		categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException());
	}

	@Transactional(readOnly = true)
	public void validateExistCategoryByOrder(Integer categoryOrder) {
		categoryRepository.findByCategoryOrder(categoryOrder).ifPresent(o -> {
			throw new ExistCategoryOrderException();
		});
	}
}
