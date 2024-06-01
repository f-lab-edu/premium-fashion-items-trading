package com.inturn.pfit.domain.item.controller;


import com.inturn.pfit.domain.item.dto.request.CreateItemRequestDTO;
import com.inturn.pfit.domain.item.dto.request.ItemPagingRequestDTO;
import com.inturn.pfit.domain.item.dto.request.ModifyItemRequestDTO;
import com.inturn.pfit.domain.item.dto.response.CreateItemResponseDTO;
import com.inturn.pfit.domain.item.dto.response.ItemResponseDTO;
import com.inturn.pfit.domain.item.facade.CreateItemFacade;
import com.inturn.pfit.domain.item.facade.ModifyItemFacade;
import com.inturn.pfit.domain.item.service.ItemQueryService;
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
@RequestMapping("/v1/item")
@RequiredArgsConstructor
public class ItemV1Controller {

	private final ItemQueryService itemQueryService;

	private final CreateItemFacade createItemFacade;

	private final ModifyItemFacade modifyItemFacade;


	//상품 단일 조회
	@GetMapping("/{ItemId}")
	public ResponseEntity<DataResponseDTO<ItemResponseDTO>> getItemById(@PathVariable @NotNull Long ItemId) {
		return DataResponseDTO.ok(itemQueryService.getItem(ItemId));
	}
	
	//상품 조회 Paging
	@GetMapping("/paging")
	public ResponseEntity<DataResponseDTO<Page<ItemResponseDTO>>> getItemPagingList(ItemPagingRequestDTO req, Pageable page) {
		return DataResponseDTO.ok(itemQueryService.getItemPagingList(req, page));
	}

	//상품 등록
	@PostMapping
	public ResponseEntity<DataResponseDTO<CreateItemResponseDTO>> createItem(@RequestBody @Valid CreateItemRequestDTO req) {
		return DataResponseDTO.ok(createItemFacade.createItem(req));
	}

	//상품 편집
	@PatchMapping
	public ResponseEntity<DataResponseDTO<ItemResponseDTO>> modifyItem(@RequestBody @Valid ModifyItemRequestDTO req) {
		return DataResponseDTO.ok(modifyItemFacade.modifyItem(req));
	}

}
