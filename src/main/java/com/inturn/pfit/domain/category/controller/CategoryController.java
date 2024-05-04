package com.inturn.pfit.domain.category.controller;

import com.inturn.pfit.domain.category.dto.request.CategoryPagingRequestDTO;
import com.inturn.pfit.domain.category.dto.request.CreateCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.request.DeleteCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.request.ModifyCategoryRequestDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryPagingResponseDTO;
import com.inturn.pfit.domain.category.dto.response.CategoryResponseDTO;
import com.inturn.pfit.domain.category.dto.response.CreateCategoryResponseDTO;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.global.common.dto.response.CommonResponseDTO;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import com.inturn.pfit.global.config.security.define.RoleConsts;
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
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryQueryService categoryQueryService;

	private final CategoryCommandService categoryCommandService;

	@GetMapping("/{categoryId}")
	public ResponseEntity<DataResponseDTO<CategoryResponseDTO>> getCategoryById(@PathVariable @NotNull Integer categoryId) {
		return ResponseEntity.ok(new DataResponseDTO<>(categoryQueryService.getCategory(categoryId)));
	}

	//카테고리 등록
	@PostMapping
	public ResponseEntity<DataResponseDTO<CreateCategoryResponseDTO>> createCategory(@RequestBody @Valid CreateCategoryRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(categoryCommandService.createCategory(req)));
	}

	//카테고리 편집
	@PatchMapping
	public ResponseEntity<DataResponseDTO<CategoryResponseDTO>> modifyCategory(@RequestBody @Valid ModifyCategoryRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(categoryCommandService.modifyCategory(req)));
	}

	//카테고리 삭제
	@DeleteMapping
	public ResponseEntity<CommonResponseDTO> deleteCategory(@RequestBody @Valid DeleteCategoryRequestDTO req) {
		//TODO - 현재 삭제 메소드는 하위 테이블인 제품에 categoryId를 FK 로 제공하기 떄문에 
		//제품 개발이 된 후 제품에 categoryId가 존재하는 경우 validate 처리
		return ResponseEntity.ok(categoryCommandService.deleteCategory(req));
	}

	//카테고리 조회 Paging
	@GetMapping("/paging")
	public ResponseEntity<Page<CategoryPagingResponseDTO>> getCategoryPagingList(CategoryPagingRequestDTO req, Pageable page) {
		return ResponseEntity.ok(categoryQueryService.getCategoryPagingList(req, page));
	}
	
	//TODO category 순번을 변경하는 부분도 추후 확인
}
