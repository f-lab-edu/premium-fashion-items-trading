package com.inturn.pfit.domain.size.dto.response;

import com.inturn.pfit.global.common.dto.response.CommonTimeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SizePagingResponseDTO extends CommonTimeDTO {

	private Integer sizeId;
	private String sizeName;

}
