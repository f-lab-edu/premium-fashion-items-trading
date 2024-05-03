package com.inturn.pfit.domain.category.service;

import com.inturn.pfit.domain.category.dto.response.CategoryResponseDTO;
import com.inturn.pfit.domain.category.entity.Category;
import com.inturn.pfit.domain.category.repository.CategoryRepository;
import com.inturn.pfit.global.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryQueryService {

	private final CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public CategoryResponseDTO getCategoryById(Integer categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException());
		return CategoryResponseDTO.from(category);
	}
}
