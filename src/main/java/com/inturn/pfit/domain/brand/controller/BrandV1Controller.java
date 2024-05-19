package com.inturn.pfit.domain.brand.controller;


import com.inturn.pfit.domain.brand.dto.request.BrandPagingRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.CreateBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.ModifyBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandPagingResponseDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandResponseDTO;
import com.inturn.pfit.domain.brand.dto.response.CreateBrandResponseDTO;
import com.inturn.pfit.domain.brand.service.BrandCommandService;
import com.inturn.pfit.domain.brand.service.BrandQueryService;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured(RoleConsts.ROLE_ADMIN)
@RestController
@RequestMapping("/v1/brand")
@RequiredArgsConstructor
public class BrandV1Controller {

	private final BrandQueryService brandQueryService;

	private final BrandCommandService brandCommandService;

	//브랜드 단일 조회
	@GetMapping("/{brandId}")
	public ResponseEntity<DataResponseDTO<BrandResponseDTO>> getBrandById(@PathVariable @Valid @NotNull Integer brandId) {
		return DataResponseDTO.ok(brandQueryService.getBrand(brandId));
	}

	//브랜드 등록
	@PostMapping
	public ResponseEntity<DataResponseDTO<CreateBrandResponseDTO>> createBrand(@RequestBody @Valid CreateBrandRequestDTO req) {
		return DataResponseDTO.ok(brandCommandService.createBrand(req));
	}

	//브랜드 편집
	@PatchMapping
	public ResponseEntity<DataResponseDTO<BrandResponseDTO>> modifyBrand(@RequestBody @Valid ModifyBrandRequestDTO req) {
		return DataResponseDTO.ok(brandCommandService.modifyBrand(req));
	}

	//브랜드 조회 Paging
	@GetMapping("/paging")
	public ResponseEntity<DataResponseDTO<Page<BrandPagingResponseDTO>>> getBrandPagingList(BrandPagingRequestDTO req, Pageable page) {
		return DataResponseDTO.ok(brandQueryService.getBrandPagingList(req, page));
	}

	//TODO - 추후 상품 개발 완료 후 삭제 메소드 추가 개발
	//브랜드 삭제
//	@DeleteMapping("/v1/brand")
//	public ResponseEntity<CommonResponseDTO> deleteBrand(@RequestBody @Valid DeleteBrandRequestDTO req) {
//
//		//제품 개발이 된 후 제품에 brandId가 존재하는 경우 validate 처리
//		return ResponseEntity.ok(brandCommandService.deleteBrand(req));
//	}
}
