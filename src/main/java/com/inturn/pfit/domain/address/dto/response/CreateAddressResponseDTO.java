package com.inturn.pfit.domain.address.dto.response;

import com.inturn.pfit.domain.address.entity.AddressEntity;
import lombok.Builder;

@Builder
public record CreateAddressResponseDTO(
		Long addressId
) {


	public static CreateAddressResponseDTO from(AddressEntity entity) {
		return CreateAddressResponseDTO.builder()
				.addressId(entity.getAddressId())
				.build();
	}
}
