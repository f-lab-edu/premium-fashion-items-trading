package com.inturn.pfit.domain.address.dto.request;

import com.inturn.pfit.domain.address.entity.AddressEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ModifyAddressRequestDTO(

		@NotNull
		Long addressId,
		@NotNull
		Long userId,

		@NotEmpty
		String recipients,

		@NotEmpty
		@Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
		String phone,

		@NotEmpty
		@Pattern(regexp = "^(\\d{5,6})$", message = "5 ~ 6 자리의 숫자만 입력 가능합니다.")
		String postCode,

		@NotEmpty
		String address,

		String addressDetail,

		@NotEmpty
		@Size(max = 1)
		String defaultYn

) {

	public AddressEntity convertAddress(AddressEntity entity) {
		return entity.builder()
				.recipients(recipients())
				.phone(phone())
				.postCode(postCode())
				.address(address())
				.addressDetail(addressDetail())
				.defaultYn(defaultYn())
				.build();
	}

}
