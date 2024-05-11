package com.inturn.pfit.domain.brand.repository;

import com.inturn.pfit.domain.brand.dto.request.BrandPagingRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandPagingResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BrandRepositoryDsl {

	Page<BrandPagingResponseDTO> getPagingList(BrandPagingRequestDTO req, Pageable pageable);
}
