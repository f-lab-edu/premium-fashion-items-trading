package com.inturn.pfit.domain.category.repository;

import com.inturn.pfit.domain.category.dto.request.CategoryPagingRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryPagingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryDsl {

	Page<CategoryPagingResponseDTO> getPagingList(CategoryPagingRequestDTO req, Pageable pageable);
}
