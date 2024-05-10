package com.inturn.pfit.domain.size.controller;



import com.inturn.pfit.domain.size.dto.request.CreateSizeRequestDTO;
import com.inturn.pfit.domain.size.dto.request.ModifySizeRequestDTO;
import com.inturn.pfit.domain.size.dto.request.SizePagingRequestDTO;
import com.inturn.pfit.domain.size.dto.response.CreateSizeResponseDTO;
import com.inturn.pfit.domain.size.dto.response.SizePagingResponseDTO;
import com.inturn.pfit.domain.size.dto.response.SizeResponseDTO;
import com.inturn.pfit.domain.size.service.SizeCommandService;
import com.inturn.pfit.domain.size.service.SizeQueryService;
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
@RequestMapping("/v1/size")
@RequiredArgsConstructor
public class SizeV1Controller {

	private final SizeQueryService sizeQueryService;

	private final SizeCommandService sizeCommandService;

	//브랜드 단일 조회
	@GetMapping("/{sizeId}")
	public ResponseEntity<DataResponseDTO<SizeResponseDTO>> getSizeById(@PathVariable @Valid @NotNull Integer sizeId) {
		return ResponseEntity.ok(new DataResponseDTO<>(sizeQueryService.getSize(sizeId)));
	}

	//브랜드 등록
	@PostMapping
	public ResponseEntity<DataResponseDTO<CreateSizeResponseDTO>> createSize(@RequestBody @Valid CreateSizeRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(sizeCommandService.createSize(req)));
	}

	//브랜드 편집
	@PatchMapping
	public ResponseEntity<DataResponseDTO<SizeResponseDTO>> modifySize(@RequestBody @Valid ModifySizeRequestDTO req) {
		return ResponseEntity.ok(new DataResponseDTO<>(sizeCommandService.modifySize(req)));
	}

	//브랜드 조회 Paging
	@GetMapping("/paging")
	public ResponseEntity<DataResponseDTO<Page<SizePagingResponseDTO>>> getSizePagingList(SizePagingRequestDTO req, Pageable page) {
		return ResponseEntity.ok(new DataResponseDTO<>(sizeQueryService.getSizePagingList(req, page)));
	}
}
