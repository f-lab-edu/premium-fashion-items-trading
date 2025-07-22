package com.inturn.pfit.domain.brand.service;

import com.inturn.pfit.domain.brand.dto.request.CreateBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.ModifyBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandResponseDTO;
import com.inturn.pfit.domain.brand.dto.response.CreateBrandResponseDTO;
import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.domain.brand.exception.BrandNotFoundException;
import com.inturn.pfit.domain.brand.repository.BrandRepository;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BrandCommandService {

	private final BrandRepository brandRepository;

	private final BrandQueryService brandQueryService;

	//브랜드 등록
	@Transactional
	public CreateBrandResponseDTO createBrand(CreateBrandRequestDTO req) {

		Brand brand = req.createBrand();
		System.out.printf("s");

		return CreateBrandResponseDTO.from(this.save(brand));
	}

	@Transactional
	public BrandResponseDTO modifyBrand(ModifyBrandRequestDTO req) {

		//해당 브랜드가 존재하는지 조회
		Brand brand = brandQueryService.getBrandById(req.brandId()).orElseThrow(() -> new BrandNotFoundException());

		Brand modBrand = req.modifyBrand(brand);

		return BrandResponseDTO.from(this.save(modBrand));
	}

	@Transactional
	public CommonResponseDTO deleteBrand(Integer brandId) {
		//브랜드 ID로 조회하고 삭제 처리
//		brandRepository.delete(brandQueryService.getBrandById(brandId));
		return CommonResponseDTO.ok();
	}

	@Transactional
	public Brand save(Brand entity) {
		return brandRepository.save(entity);
	}
}
