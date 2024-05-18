package com.inturn.pfit.domain.address.dto.response;

import com.inturn.pfit.domain.address.entity.AddressEntity;
import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponseDTO extends CommonTimeDTO {

	private Long addressId;

	private Long userId;

	private String recipients;

	private String phone;

	private String postCode;

	private String address;

	private String addressDetail;

	private String defaultYn;

	public static AddressResponseDTO from(AddressEntity entity) {
		return AddressResponseDTO.builder()
				.addressId(entity.getAddressId())
				.userId(entity.getUserId())
				.recipients(entity.getRecipients())
				.phone(entity.getPhone())
				.postCode(entity.getPostCode())
				.address(entity.getAddress())
				.addressDetail(entity.getAddressDetail())
				.defaultYn(entity.getDefaultYn())
				.build();
	}
}

