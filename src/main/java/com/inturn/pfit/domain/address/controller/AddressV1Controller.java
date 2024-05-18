package com.inturn.pfit.domain.address.controller;

import com.inturn.pfit.domain.address.dto.response.AddressResponseDTO;
import com.inturn.pfit.domain.address.service.AddressCommandService;
import com.inturn.pfit.domain.address.service.AddressQueryService;
import com.inturn.pfit.global.common.dto.response.DataResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/address")
@RequiredArgsConstructor
public class AddressV1Controller {

	private final AddressQueryService addressQueryService;

	private final AddressCommandService addressCommandService;

	//주소록 조회
	@GetMapping("/{addressId}")
	public ResponseEntity<DataResponseDTO<AddressResponseDTO>> getAddressById(@PathVariable @Valid @NotNull Long addressId) {
		return DataResponseDTO.ok(addressQueryService.getAddressById(addressId));
	}

	//주소록 등록
//	@PostMapping
//	public ResponseEntity<DataResponseDTO<CreateAddressResponseDTO>> createAddress(@RequestBody @Valid CreateAddressRequestDTO req) {
//		return DataResponseDTO.ok(addressCommandService.createAddress(req));
//	}
//
//	//주소록 편집
//	@PatchMapping
//	public ResponseEntity<DataResponseDTO<AddressResponseDTO>> modifyAddress(@RequestBody @Valid ModifyAddressRequestDTO req) {
//		return DataResponseDTO.ok(addressCommandService.modifyAddress(req));
//	}

	//주소록 사용자 조회
	@GetMapping("/user/{userId}")
	public ResponseEntity<DataResponseDTO<List<AddressResponseDTO>>> getAddressByUserId(@PathVariable @Valid @NotNull Long userId) {
		return DataResponseDTO.ok(addressQueryService.getAddressListByUserId(userId));
	}

}
