package com.inturn.pfit.domain.brand.service;

import com.inturn.pfit.domain.brand.dto.response.BrandResponseDTO;
import com.inturn.pfit.domain.brand.entity.Brand;
import com.inturn.pfit.domain.brand.repository.BrandRepository;
import com.inturn.pfit.global.common.exception.NotFoundException;
import com.inturn.pfit.global.common.exception.vo.CommonErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BrandQueryServiceTest {

	@InjectMocks
	BrandQueryService brandQueryService;

	@Mock
	BrandRepository brandRepository;

	String brandName = "Nike";
	Integer brandId = 1;

	Brand brand;

	@BeforeEach
	void testStart() {
		brand = Brand.builder()
				.brandId(brandId)
				.brandName(brandName)
				.build();
	}

	@Test
	@DisplayName("브랜드 조회(getBrandById) - 성공")
	void getBrandById_Success(){

		//given
		when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

		//when
		Brand result = brandQueryService.getBrandById(brandId);

		//then
		assertEquals(result.getBrandId(), brandId);
	}

	@Test
	@DisplayName("브랜드 조회(getBrandById) - 브랜드가 존재하지 않음.")
	void getBrandById_Fail_NotFoundBrand(){

		//given
		when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

		//when
		final NotFoundException result =  assertThrows(NotFoundException.class, () -> brandQueryService.getBrandById(brandId));

		//then
		assertEquals(result.getMessage(), CommonErrorCode.NOT_FOUND_EXCEPTION.getErrorMessage());
	}

	@Test
	@DisplayName("브랜드 조회(getBrand) - 성공")
	void getBrand_Success(){

		//given
		when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

		//when
		BrandResponseDTO result = brandQueryService.getBrand(brandId);

		//then
		assertEquals(result.brandId(), brandId);
		assertEquals(result.brandName(), brandName);
	}

}