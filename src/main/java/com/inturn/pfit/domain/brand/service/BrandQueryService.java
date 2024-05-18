package com.inturn.pfit.domain.brand.service;

import com.inturn.pfit.domain.brand.dto.request.BrandPagingRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandPagingResponseDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandResponseDTO;
import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.domain.brand.exception.BrandNotFoundException;
import com.inturn.pfit.domain.brand.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandQueryService {
	private final BrandRepository brandRepository;

	@Transactional(readOnly = true)
	public Page<BrandPagingResponseDTO> getBrandPagingList(BrandPagingRequestDTO req, Pageable page) {
		return brandRepository.getPagingList(req, page);
	}

	@Transactional(readOnly = true)
	public BrandResponseDTO getBrand(Integer brandId) {
		return BrandResponseDTO.from(getBrandById(brandId));
	}

	@Transactional(readOnly = true)
	public Brand getBrandById(Integer brandId) {
		return brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException());
	}

	@Transactional(readOnly = true)
	public void validateExistBrandById(Integer brandId) {
		brandRepository.findById(brandId).orElseThrow(() -> new BrandNotFoundException());
	}
}
