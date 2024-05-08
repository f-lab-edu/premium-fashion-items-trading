package com.inturn.pfit.domain.brand.controller;


import com.inturn.pfit.domain.brand.dto.request.BrandPagingRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.CreateBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.DeleteBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.request.ModifyBrandRequestDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandPagingResponseDTO;
import com.inturn.pfit.domain.brand.dto.response.BrandResponseDTO;
import com.inturn.pfit.domain.brand.dto.response.CreateBrandResponseDTO;
import com.inturn.pfit.domain.brand.service.BrandCommandService;
import com.inturn.pfit.domain.brand.service.BrandQueryService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
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
@RequiredArgsConstructor
public class BrandController {

	private final BrandQueryService brandQueryService;

	private final BrandCommandService brandCommandService;

	@GetMapping("/v1/brand/{brandId}")
	public ResponseEntity<DataResponseDTO<BrandResponseDTO>> getBrandByIdV1(@PathVariable @Valid @NotNull Integer brandId) {
		return ResponseEntity.ok(new DataResponseDTO<>(brandQueryService.getBrand(brandId)));
	}

	//브랜드 등록
	@PostMapping("/v1/brand")
	public ResponseEntity<DataResponseDTO<CreateBrandResponseDTO>> createBrandV1(@RequestBody @Valid CreateBrandRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(brandCommandService.createBrand(req)));
	}

	//브랜드 편집
	@PatchMapping("/v1/brand")
	public ResponseEntity<DataResponseDTO<BrandResponseDTO>> modifyBrandV1(@RequestBody @Valid ModifyBrandRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(brandCommandService.modifyBrand(req)));
	}

	//브랜드 삭제
	@DeleteMapping("/v1/brand")
	public ResponseEntity<CommonResponseDTO> deleteBrandV1(@RequestBody @Valid DeleteBrandRequestDTO req) {
		//TODO - 현재 삭제 메소드는 하위 테이블인 제품에 brandId를 FK 로 제공하기 떄문에 
		//제품 개발이 된 후 제품에 brandId가 존재하는 경우 validate 처리
		return ResponseEntity.ok(brandCommandService.deleteBrand(req));
	}

	//브랜드 조회 Paging
	@GetMapping("/v1/brand/paging")
	public ResponseEntity<DataResponseDTO<Page<BrandPagingResponseDTO>>> getBrandPagingListV1(BrandPagingRequestDTO req, Pageable page) {
		return ResponseEntity.ok(new DataResponseDTO<>(brandQueryService.getBrandPagingList(req, page)));
	}
}
