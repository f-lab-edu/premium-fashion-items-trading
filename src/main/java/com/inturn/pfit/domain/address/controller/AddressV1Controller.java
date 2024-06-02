package com.inturn.pfit.domain.address.controller;

import com.inturn.pfit.domain.address.dto.request.CreateAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.request.ModifyAddressRequestDTO;
import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.dto.response.CreateAddressResponseDTO;
import com.inturn.pfit.domain.address.facade.CreateAddressFacade;
import com.inturn.pfit.domain.address.facade.GetUserAddressFacade;
import com.inturn.pfit.domain.address.facade.ModifyAddressFacade;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import com.inturn.pfit.global.config.security.vo.RoleConsts;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured(RoleConsts.ROLE_USER)
@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressV1Controller {

	private final AddressQueryService addressQueryService;

	private final ModifyAddressFacade modifyAddressFacade;

	private final CreateAddressFacade createAddressFacade;

	private final GetUserAddressFacade getUserAddressFacade;

	//주소 조회
	@GetMapping("/{addressId}")
	public ResponseEntity<DataResponseDTO<AddressResponseDTO>> getAddressById(@PathVariable @Valid @NotNull Long addressId) {
		return DataResponseDTO.ok(addressQueryService.getAddress(addressId));
	}

	//주소 등록
	@PostMapping
	public ResponseEntity<DataResponseDTO<CreateAddressResponseDTO>> createAddress(@RequestBody @Valid CreateAddressRequestDTO req) {
		return DataResponseDTO.ok(createAddressFacade.createAddress(req));
	}

	//주소 편집
	@PatchMapping
	public ResponseEntity<DataResponseDTO<AddressResponseDTO>> modifyAddress(@RequestBody @Valid ModifyAddressRequestDTO req) {
		return DataResponseDTO.ok(modifyAddressFacade.modifyAddress(req));
	}

	//사용자 주소록 목록 조회
	@GetMapping("/user")
	public ResponseEntity<DataResponseDTO<List<AddressResponseDTO>>> getAddressByUserId() {
		return DataResponseDTO.ok(getUserAddressFacade.getAddressListByUserId());
	}

}
