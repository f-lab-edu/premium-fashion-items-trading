package com.inturn.pfit.domain.category.controller;

import com.inturn.pfit.domain.category.dto.response.CategoryResponseDTO;
import com.inturn.pfit.domain.category.service.CategoryCommandService;
import com.inturn.pfit.domain.category.service.CategoryQueryService;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import com.inturn.pfit.global.config.security.define.RoleConsts;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured(RoleConsts.ROLE_ADMIN)
@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryQueryService categoryQueryService;

	private final CategoryCommandService categoryCommandService;

	@GetMapping("/{categoryId}")
	public ResponseEntity<DataResponseDTO<CategoryResponseDTO>> getCategoryById(@PathVariable @NotNull Integer categoryId) {
		return ResponseEntity.ok(new DataResponseDTO<>(categoryQueryService.getCategoryById(categoryId)));
	}
}
